package com.muasdev.moviedb_android.data.model.movie_reviews

data class MovieReviews(
    val id: Int? = null,
    val page: Int? = null,
    val results: List<Result?>? = null,
    val totalPages: Int? = null,
    val totalResults: Int? = null
)