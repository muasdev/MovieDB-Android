package com.muasdev.moviedb_android.ui.detail

sealed class DetailEvent {
    data class LoadMovieDetails(
        val movieId: Int
    ): DetailEvent()
}