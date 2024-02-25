package com.muasdev.moviedb_android.domain.usecase

import com.muasdev.moviedb_android.domain.repository.MoviePagingRepository
import javax.inject.Inject

class GetDiscoverMoviesPagingByGenreUseCase @Inject constructor(
    private val moviePagingRepository: MoviePagingRepository
) {
    suspend operator fun invoke(page: Int?, genreId: String?) =
        moviePagingRepository.getPagingDiscoverMovieByGenre(
            page = if (page == 0) null else page,
            genreId = genreId
        )
}