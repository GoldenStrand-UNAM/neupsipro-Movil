package com.example.neupsipromovil.data.remote.dto

import com.google.gson.annotations.SerializedName


data class ForumResponse(
    @SerializedName("data") val data: ForumDataDto,
)

data class ForumDataDto(
    @SerializedName("posts")      val posts:      List<PostDto>,
    @SerializedName("pagination") val pagination: PaginationDto,
)

// ── Post ─────────────────────────────────────────────────────────────────────
data class PostDto(
    @SerializedName("id")      val id:      String,
    @SerializedName("title")   val title:   String,
    @SerializedName("content") val content: String,
    @SerializedName("image")   val image:   String?,
    @SerializedName("date")    val date:    String,
    @SerializedName("author")  val author:  String,
    @SerializedName("pp")      val pp:      String?,   // profile picture URL
)

// ── Pagination ────────────────────────────────────────────────────────────────
data class PaginationDto(
    @SerializedName("page")       val page:       Int,
    @SerializedName("limit")      val limit:      Int,
    @SerializedName("total")      val total:      Int,
    @SerializedName("totalPages") val totalPages: Int,
)
