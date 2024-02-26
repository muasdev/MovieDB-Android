package com.muasdev.moviedb_android.data.remote

import com.muasdev.moviedb_android.data.mapper.GenresMapperDtoToModel
import com.muasdev.moviedb_android.data.mapper.asDataModel
import com.muasdev.moviedb_android.data.model.genres.Genres
import com.muasdev.moviedb_android.data.model.movie_details.MovieDetails
import com.muasdev.moviedb_android.data.model.movie_videos.MovieVideos
import com.muasdev.moviedb_android.data.remote.api.MovieDbApiServices
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteMoviesSource @Inject constructor(
    private val apiServices: MovieDbApiServices
) {
    suspend fun getAllGenresForMovie(): Genres = withContext(Dispatchers.IO) {
            val response = apiServices.getAllGenresForMovieFromRemote()
            GenresMapperDtoToModel().mapFrom(response)
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails = withContext(Dispatchers.IO) {
        val response = apiServices.getMovieDetails(movieId)
        response.asDataModel()
    }

    suspend fun getMovieVideos(movieId: Int): MovieVideos = withContext(Dispatchers.IO) {
        val response = apiServices.getMovieVideos(movieId)
        response.asDataModel()
    }
}