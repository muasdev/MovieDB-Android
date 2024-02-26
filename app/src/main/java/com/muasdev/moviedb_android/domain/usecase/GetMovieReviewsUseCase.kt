package com.muasdev.moviedb_android.domain.usecase

import androidx.paging.cachedIn
import com.muasdev.moviedb_android.domain.repository.MoviePagingRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class GetMovieReviewsUseCase @Inject constructor(
    private val moviePagingRepository: MoviePagingRepository,
) {
    suspend operator fun invoke(
        movieId: Int,
        scope: CoroutineScope
    ) =
        moviePagingRepository.getPagingUserReviews(
            movieId = movieId
        ).cachedIn(scope)
}