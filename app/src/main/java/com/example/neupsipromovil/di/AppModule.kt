package com.example.neupsipromovil.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.neupsipromovil.data.local.AuthManager
import com.example.neupsipromovil.data.remote.api.APIService
import com.example.neupsipromovil.data.remote.api.AuthInterceptor
import com.example.neupsipromovil.data.remote.api.LoginApiService
import com.example.neupsipromovil.data.repository.LoginRepositoryImpl
import com.example.neupsipromovil.data.repository.ProfileRepositoryImpl
import com.example.neupsipromovil.domain.repository.LoginRepository
import com.example.neupsipromovil.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Suppress("ktlint:standard:property-naming")
    // host
    private const val url = "http://10.25.89.194:3000/"

    // Encrypted prefs for storing the JWT
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        val masterKey =
            MasterKey
                .Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        return EncryptedSharedPreferences.create(
            context,
            "auth_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    // OkHttp with auth interceptor added in for every request
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging =
            okhttp3.logging.HttpLoggingInterceptor().apply {
                level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            }
        return OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Build the LoginApiService implementation from the Retrofit
    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService = retrofit.create(LoginApiService::class.java)

    // build the LoginRepository to its concrete implementation
    @Provides
    @Singleton
    fun provideLoginRepository(
        loginApiService: LoginApiService,
        apiService: APIService,
        authManager: AuthManager,
    ): LoginRepository = LoginRepositoryImpl(loginApiService, apiService, authManager)

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): APIService = retrofit.create(APIService::class.java)

    @Provides
    @Singleton
    fun provideProfileRepository(provideApiService: APIService): ProfileRepository = ProfileRepositoryImpl(provideApiService)
}
