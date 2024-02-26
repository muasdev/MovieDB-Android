package com.muasdev.moviedb_android.domain.repository

import com.muasdev.moviedb_android.data.Resource
import com.muasdev.moviedb_android.domain.model.detail_movie.MovieDetails
import com.muasdev.moviedb_android.domain.model.genres.Genres
import com.muasdev.moviedb_android.domain.model.movie_videos.MovieVideos
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getAllGenresForMovie(): Flow<Resource<Genres>>

    suspend fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetails>>
    suspend fun getMovieVideos(movieId: Int): Flow<Resource<MovieVideos>>
}