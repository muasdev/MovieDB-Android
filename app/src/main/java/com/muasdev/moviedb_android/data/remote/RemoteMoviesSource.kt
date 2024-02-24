package com.muasdev.moviedb_android.data.remote

import com.muasdev.moviedb_android.data.mapper.DiscoverMoviesMapperDtoToModel
import com.muasdev.moviedb_android.data.mapper.GenresMapperDtoToModel
import com.muasdev.moviedb_android.data.model.discover.DiscoverMovies
import com.muasdev.moviedb_android.data.model.genres.Genres
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

    suspend fun getDiscoverMovieByGenre(genreId: String?): DiscoverMovies = withContext(Dispatchers.IO) {
        val response = apiServices.getDiscoverMovieByGenreFromRemote(genreId)
        DiscoverMoviesMapperDtoToModel().mapFrom(response)
    }
}