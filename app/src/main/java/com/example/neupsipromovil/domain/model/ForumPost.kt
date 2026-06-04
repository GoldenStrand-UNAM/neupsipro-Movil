package com.example.neupsipromovil.domain.model

data class ForumPost(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val date: String,
    val author: String,
    val avatarUrl: String?,
)

data class ForumPage(
    val posts: List<ForumPost>,
    val page: Int,
    val totalPages: Int,
)
