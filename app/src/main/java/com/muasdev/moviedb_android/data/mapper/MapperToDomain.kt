package com.muasdev.moviedb_android.data.mapper

import com.muasdev.moviedb_android.common.Mapper
import com.muasdev.moviedb_android.data.model.discover.Result
import com.muasdev.moviedb_android.data.model.genres.Genre
import com.muasdev.moviedb_android.data.model.genres.Genres
import com.muasdev.moviedb_android.data.model.movie_details.MovieDetails
import com.muasdev.moviedb_android.domain.model.detail_movie.MovieTrailer
import com.muasdev.moviedb_android.domain.model.detail_movie.MovieDetails as MovieDetailsDomain
import com.muasdev.moviedb_android.domain.model.discover.Result as ResultMovieDomain
import com.muasdev.moviedb_android.domain.model.genres.Genre as GenreDomain
import com.muasdev.moviedb_android.domain.model.genres.Genres as GenresDomain

/*mapper for genres*/
class GenresMapperToDomain: Mapper<Genres, GenresDomain> {
    override fun mapFrom(from: Genres): GenresDomain {
        return GenresDomain(
            genres = from.genres?.map {
                it?.let { genre ->
                    GenreMapperToDomain().mapFrom(genre)
                }
            }
        )
    }
}

class GenreMapperToDomain: Mapper<Genre, GenreDomain> {
    override fun mapFrom(from: Genre): GenreDomain {
        return GenreDomain(
            id = from.id,
            name = from.name
        )
    }
}

class ResultMoviesMapperToDomain : Mapper<Result, ResultMovieDomain> {
    override fun mapFrom(from: Result): ResultMovieDomain {
        return ResultMovieDomain(
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

fun MovieDetails.asDomainModel(movieTrailer: MovieTrailer): MovieDetailsDomain {
    return MovieDetailsDomain(
        backdropPath = backdropPath,
        homepage = homepage,
        id = id,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        status = status,
        tagline = tagline,
        title = title,
        movieTrailer = movieTrailer
    )
}