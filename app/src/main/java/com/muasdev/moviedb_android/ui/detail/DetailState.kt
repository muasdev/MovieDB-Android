package com.muasdev.moviedb_android.ui.detail

import com.muasdev.moviedb_android.domain.model.detail_movie.MovieDetails
import com.muasdev.moviedb_android.domain.model.detail_movie.MovieTrailer

data class DetailState (
    val isLoading: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val movieTrailer: MovieTrailer? = null,
    val errorMessage: String? = null,
)