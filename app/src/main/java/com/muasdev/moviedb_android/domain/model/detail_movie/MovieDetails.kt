package com.muasdev.moviedb_android.domain.model.detail_movie

data class MovieDetails(
    val backdropPath: String? = null,
    val homepage: String? = null,
    val id: Int? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val movieTrailer: MovieTrailer? = null,
)