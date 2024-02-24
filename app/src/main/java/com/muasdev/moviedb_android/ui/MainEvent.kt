package com.muasdev.moviedb_android.ui

sealed class MainEvent {
    data class FilterMoviesByGenre(
        val genreId: String?
    ): MainEvent()
}