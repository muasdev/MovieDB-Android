package com.muasdev.moviedb_android.ui.main

sealed class MainEvent {
    data class FilterMoviesByGenre(
        val genreId: String?
    ): MainEvent()
}