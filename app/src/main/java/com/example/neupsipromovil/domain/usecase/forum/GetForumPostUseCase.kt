package com.example.neupsipromovil.domain.usecase.forum

import com.example.neupsipromovil.data.repository.ForumRepository
import com.example.neupsipromovil.domain.model.ForumPage
import javax.inject.Inject

class GetForumPostsUseCase @Inject constructor(
    private val repository: ForumRepository,
) {
    suspend operator fun invoke(page: Int = 1, limit: Int = 10): Result<ForumPage> =
        repository.getPosts(page = page, limit = limit)
}
