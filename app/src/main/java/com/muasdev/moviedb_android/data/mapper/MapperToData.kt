package com.muasdev.moviedb_android.data.mapper

import com.muasdev.moviedb_android.common.Mapper
import com.muasdev.moviedb_android.data.model.discover.DiscoverMovies
import com.muasdev.moviedb_android.data.model.discover.Result
import com.muasdev.moviedb_android.data.model.genres.Genre
import com.muasdev.moviedb_android.data.model.genres.Genres
import com.muasdev.moviedb_android.data.remote.dto.discover.DiscoverMoviesDto
import com.muasdev.moviedb_android.data.remote.dto.discover.ResultDto
import com.muasdev.moviedb_android.data.remote.dto.genres.GenreDto
import com.muasdev.moviedb_android.data.remote.dto.genres.GenresDto

/*mapper for genres*/
class GenresMapperDtoToModel: Mapper<GenresDto, Genres> {
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

class GenreMapperDtoToModel: Mapper<GenreDto, Genre> {
    override fun mapFrom(from: GenreDto): Genre {
        return Genre(
            id = from.id,
            name = from.name
        )
    }
}

/*mapper for discover movie*/
class DiscoverMoviesMapperDtoToModel: Mapper<DiscoverMoviesDto, DiscoverMovies> {
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

class ResultMapperDtoToModel(): Mapper<ResultDto, Result> {
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