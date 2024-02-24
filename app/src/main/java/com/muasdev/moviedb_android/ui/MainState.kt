package com.muasdev.moviedb_android.ui

import com.muasdev.moviedb_android.domain.model.discover.DiscoverMovies
import com.muasdev.moviedb_android.domain.model.genres.Genres

data class MainState (
    val isGenresLoading: Boolean = false,
    val isDiscoverLoading: Boolean = false,
    val genres: Genres? = null,
    val discoverMovies: DiscoverMovies? = null,
    val errorMessage: String? = null,
)