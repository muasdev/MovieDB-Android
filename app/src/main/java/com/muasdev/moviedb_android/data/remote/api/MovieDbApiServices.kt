package com.muasdev.moviedb_android.data.remote.api

import com.muasdev.moviedb_android.data.remote.dto.discover.DiscoverMoviesDto
import com.muasdev.moviedb_android.data.remote.dto.genres.GenresDto
import com.muasdev.moviedb_android.data.remote.dto.movie_details.MovieDetailsDto
import com.muasdev.moviedb_android.data.remote.dto.movie_reviews.MovieReviewsDto
import com.muasdev.moviedb_android.data.remote.dto.movie_videos.MovieVideosDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApiServices {

    @GET("genre/movie/list")
    suspend fun getAllGenresForMovieFromRemote(): GenresDto

    @GET("discover/movie")
    suspend fun getPagingDiscoverMovieByGenreFromRemote(
        @Query("page") page: Int? = null,
        @Query("with_genres") withGenres: String? = null,
    ): DiscoverMoviesDto

    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideos(
        @Path("movieId") movieId: Int,
    ): MovieVideosDto

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int
    ): MovieDetailsDto

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieReviewsDto

    companion object {
        const val BASE_URL: String = "https://api.themoviedb.org/3/"
    }
}
