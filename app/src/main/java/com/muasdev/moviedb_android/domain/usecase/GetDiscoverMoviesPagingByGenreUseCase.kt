package com.muasdev.moviedb_android.domain.usecase

import androidx.paging.cachedIn
import com.muasdev.moviedb_android.domain.repository.MoviePagingRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope


class GetDiscoverMoviesPagingByGenreUseCase @Inject constructor(
    private val moviePagingRepository: MoviePagingRepository,
) {
    suspend operator fun invoke(
        page: Int?, genreId: String?,
        scope: CoroutineScope
    ) =
        moviePagingRepository.getPagingDiscoverMovieByGenre(
            page = if (page == 0) null else page,
            genreId = genreId
        ).cachedIn(scope)
}