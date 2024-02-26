package com.muasdev.moviedb_android.domain.usecase

import com.muasdev.moviedb_android.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieVideosUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = repository.getMovieVideos(movieId)
}