package com.example.neupsipromovil.domain.mapper

import com.example.neupsipromovil.data.remote.dto.ForumResponse
import com.example.neupsipromovil.data.remote.dto.PostDto
import com.example.neupsipromovil.domain.model.ForumPage
import com.example.neupsipromovil.domain.model.ForumPost

fun PostDto.toDomain(): ForumPost =
    ForumPost(
        id        = id,
        title     = title,
        content   = content,
        imageUrl  = image,
        date      = date,
        author    = author,
        avatarUrl = pp,
    )

fun ForumResponse.toDomain(): ForumPage =
    ForumPage(
        posts      = data.posts.map { it.toDomain() },
        page       = data.pagination.page,
        totalPages = data.pagination.totalPages,
    )
