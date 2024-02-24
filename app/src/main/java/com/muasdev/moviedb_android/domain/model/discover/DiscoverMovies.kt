package com.muasdev.moviedb_android.domain.model.discover

data class DiscoverMovies(
    val page: Int? = null,
    val results: List<Result?>? = null,
    val totalPages: Int? = null,
    val totalResults: Int? = null
)