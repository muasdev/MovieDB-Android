package com.muasdev.moviedb_android.domain.repository

import com.muasdev.moviedb_android.data.Resource
import com.muasdev.moviedb_android.domain.model.genres.Genres
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getAllGenresForMovie(): Flow<Resource<Genres>>
}