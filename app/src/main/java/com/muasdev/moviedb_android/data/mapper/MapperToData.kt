package com.muasdev.moviedb_android.data.mapper

import com.muasdev.moviedb_android.common.Mapper
import com.muasdev.moviedb_android.data.model.discover.DiscoverMovies
import com.muasdev.moviedb_android.data.model.discover.Result
import com.muasdev.moviedb_android.data.model.genres.Genre
import com.muasdev.moviedb_android.data.model.genres.Genres
import com.muasdev.moviedb_android.data.model.movie_details.MovieDetails
import com.muasdev.moviedb_android.data.model.movie_reviews.MovieReviews
import com.muasdev.moviedb_android.data.model.movie_videos.MovieVideos
import com.muasdev.moviedb_android.data.remote.dto.discover.DiscoverMoviesDto
import com.muasdev.moviedb_android.data.remote.dto.discover.ResultDto
import com.muasdev.moviedb_android.data.remote.dto.genres.GenreDto
import com.muasdev.moviedb_android.data.remote.dto.genres.GenresDto
import com.muasdev.moviedb_android.data.remote.dto.movie_details.MovieDetailsDto
import com.muasdev.moviedb_android.data.remote.dto.movie_reviews.MovieReviewsDto
import com.muasdev.moviedb_android.data.remote.dto.movie_videos.MovieVideosDto
import com.muasdev.moviedb_android.data.model.movie_reviews.MovieReviewResultData as MovieReviewResult
import com.muasdev.moviedb_android.data.model.movie_videos.MovieVideoResultData as MovieVideosResult
import com.muasdev.moviedb_android.data.remote.dto.movie_reviews.Result as MovieReviewResultDto
import com.muasdev.moviedb_android.data.remote.dto.movie_videos.Result as MovieVideosResultDto

/*mapper for genres*/
class GenresMapperDtoToModel : Mapper<GenresDto, Genres> {
    override fun mapFrom(from: GenresDto): Genres {
        return Genres(
            genres = from.genres?.map {
                it?.let { genre ->
                    GenreMapperDtoToModel().mapFrom(genre)
                }
            }
        )
    }
}

class GenreMapperDtoToModel : Mapper<GenreDto, Genre> {
    override fun mapFrom(from: GenreDto): Genre {
        return Genre(
            id = from.id,
            name = from.name
        )
    }
}

/*mapper for discover movie*/
class DiscoverMoviesMapperDtoToModel : Mapper<DiscoverMoviesDto, DiscoverMovies> {
    override fun mapFrom(from: DiscoverMoviesDto): DiscoverMovies {
        return DiscoverMovies(
            page = from.page,
            totalPages = from.totalPages,
            totalResults = from.totalResults,
            results = from.results?.map {
                it?.let {
                    ResultMapperDtoToModel().mapFrom(it)
                }
            }
        )
    }
}

class ResultMapperDtoToModel() : Mapper<ResultDto, Result> {
    override fun mapFrom(from: ResultDto): Result {
        return Result(
            backdropPath = from.backdropPath,
            id = from.id,
            originalTitle = from.originalTitle,
            overview = from.overview,
            posterPath = from.posterPath,
            releaseDate = from.releaseDate,
            title = from.title,
        )
    }

}

fun MovieDetailsDto.asDataModel(): MovieDetails {
    return MovieDetails(
        backdropPath = backdropPath,
        homepage = homepage,
        id = id,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        status = status,
        tagline = tagline,
        title = title
    )
}

fun MovieReviewsDto.asDataModel(): MovieReviews {
    return MovieReviews(
        id = id,
        page = page,
        results = results?.map {
            it?.let {
                it.asDataModel()
            }
        },
        totalPages = totalPages,
        totalResults = totalResults
    )
}

fun MovieReviewResultDto.asDataModel(): MovieReviewResult {
    return MovieReviewResult(
        author = author, content = content, createdAt = createdAt, id = id, avatarPath = authorDetails?.avatarPath
    )
}

fun MovieVideosDto.asDataModel(): MovieVideos {
    return MovieVideos(
        id = id, results = results?.map {
            it?.let {
                it.asDataModel()
            }
        },
    )
}

fun MovieVideosResultDto.asDataModel(): MovieVideosResult{
    return MovieVideosResult(
        id = id, key = key, name = name, type = type
    )
}