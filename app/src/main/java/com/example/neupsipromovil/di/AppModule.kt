
package com.example.neupsipromovil.di
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.neupsipromovil.data.local.AuthManager
import com.example.neupsipromovil.data.remote.api.APIService
import com.example.neupsipromovil.data.remote.api.AuthInterceptor
import com.example.neupsipromovil.data.remote.api.ForumApiService
import com.example.neupsipromovil.data.remote.api.LoginApiService
import com.example.neupsipromovil.data.repository.LoginRepositoryImpl
import com.example.neupsipromovil.data.repository.ProfileRepositoryImpl
import com.example.neupsipromovil.data.repository.ForumRepository
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
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val URL_MAIN = "http://banu.com.mx/"
    private const val URL_FORUM = "http://banu.com.mx/"

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

    // ── OkHttp compartido con AuthInterceptor (main + forum) ─────────────────
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = okhttp3.logging.HttpLoggingInterceptor().apply {
            level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // ── Retrofit principal (banu.com.mx) ──────────────────────────────────────
    @Provides
    @Singleton
    @Named("main")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL_MAIN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // ── Retrofit foro (localhost:3000) ────────────────────────────────────────
    @Provides
    @Singleton
    @Named("forum")
    fun provideForumRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL_FORUM)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideLoginApiService(@Named("main") retrofit: Retrofit): LoginApiService =
        retrofit.create(LoginApiService::class.java)

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginApiService: LoginApiService,
        apiService: APIService,
        authManager: AuthManager,
    ): LoginRepository = LoginRepositoryImpl(loginApiService, apiService, authManager)

    @Provides
    @Singleton
    fun provideApiService(@Named("main") retrofit: Retrofit): APIService =
        retrofit.create(APIService::class.java)

    @Provides
    @Singleton
    fun provideProfileRepository(apiService: APIService): ProfileRepository =
        ProfileRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideForumApiService(@Named("forum") retrofit: Retrofit): ForumApiService =
        retrofit.create(ForumApiService::class.java)

    @Provides
    @Singleton
    fun provideForumRepository(
        forumApiService: ForumApiService,
        authManager: AuthManager,           // ← agrega esto
    ): ForumRepository = ForumRepository(forumApiService, authManager)
}
