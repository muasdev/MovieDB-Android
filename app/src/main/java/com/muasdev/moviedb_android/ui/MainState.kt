package com.muasdev.moviedb_android.ui

import androidx.paging.PagingData
import com.muasdev.moviedb_android.domain.model.discover.Result
import com.muasdev.moviedb_android.domain.model.genres.Genres

data class MainState (
    val isGenresLoading: Boolean = false,
    val isDiscoverLoading: Boolean = false,
    val genres: Genres? = null,
    val discoverMoviesPaging: PagingData<Result>? = null,
    val errorMessage: String? = null,
)