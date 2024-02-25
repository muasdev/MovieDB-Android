package com.muasdev.moviedb_android.data.remote.api

import com.muasdev.moviedb_android.data.remote.dto.discover.DiscoverMoviesDto
import com.muasdev.moviedb_android.data.remote.dto.genres.GenresDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDbApiServices {

    @GET("genre/movie/list")
    suspend fun getAllGenresForMovieFromRemote(): GenresDto

    @GET("discover/movie")
    suspend fun getPagingDiscoverMovieByGenreFromRemote(
        @Query("page") page: Int? = null,
        @Query("with_genres") withGenres: String? = null,
    ): DiscoverMoviesDto

    companion object {
        const val BASE_URL: String = "https://api.themoviedb.org/3/"
    }
}
