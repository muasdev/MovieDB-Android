package com.muasdev.moviedb_android.domain.model.movie_reviews

data class MovieReviews(
    val id: Int? = null,
    val page: Int? = null,
    val results: List<MovieReviewResults?>? = null,
    val totalPages: Int? = null,
    val totalResults: Int? = null
)