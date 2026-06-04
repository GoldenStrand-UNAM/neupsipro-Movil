package com.example.neupsipromovil.presentation.screens.forum


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neupsipromovil.domain.model.ForumPost
import com.example.neupsipromovil.domain.usecase.login.LogoutUseCase

import com.example.neupsipromovil.domain.usecase.forum.GetForumPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ForumUiState {
    object Loading : ForumUiState
    data class Success(val posts: List<ForumPost>) : ForumUiState
    data class Error(val message: String) : ForumUiState
}

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val getForumPostsUseCase: GetForumPostsUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForumUiState>(ForumUiState.Loading)
    val uiState: StateFlow<ForumUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allPosts: List<ForumPost> = emptyList()

    init {
        loadPosts()
    }

    companion object {
        const val MAX_SEARCH_LENGTH = 100
    }

    fun loadPosts(page: Int = 1, limit: Int = 10) {
        viewModelScope.launch {
            _uiState.value = ForumUiState.Loading
            getForumPostsUseCase(page = page, limit = limit)
                .onSuccess { forumPage ->
                    allPosts = forumPage.posts
                    _uiState.value = ForumUiState.Success(allPosts)
                }
                .onFailure { throwable ->
                    _uiState.value = ForumUiState.Error(
                        throwable.message ?: "Error desconocido al cargar el foro"
                    )
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        if (query.length > MAX_SEARCH_LENGTH) return

        _searchQuery.value = query
        val filtered = if (query.isBlank()) allPosts
        else allPosts.filter { post ->
            post.title.contains(query, ignoreCase = true) ||
                    post.content.contains(query, ignoreCase = true) ||
                    post.author.contains(query, ignoreCase = true)
        }
        _uiState.value = ForumUiState.Success(filtered)
    }

    fun logout(onSuccessLogout: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = ForumUiState.Loading
            logoutUseCase()
                .onSuccess {
                    onSuccessLogout()
                }
                .onFailure { exception ->
                    _uiState.value = ForumUiState.Error(
                        exception.message ?: "Error al cerrar sesión"
                    )
                }
        }
    }

    fun retry() = loadPosts()
}