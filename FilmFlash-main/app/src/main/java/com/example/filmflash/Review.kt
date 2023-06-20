package com.example.filmflash

import android.text.Spanned
import com.squareup.moshi.Json

data class Review(
    val author: String,
    @Json(name = "author_details") val authorDetails: AuthorDetails,
    val content: String,
)

data class AuthorDetails(
    val name: String,
    val username: String,
    @Json(name = "avatar_path") var avatarPath: String?,
    val rating: Double?
)

data class ReviewWithAll(
    val author: String,
    val authorDetails: AuthorDetails,
    val content: String,
    var formattedContent: Spanned,
    var contentPreview: String
)
