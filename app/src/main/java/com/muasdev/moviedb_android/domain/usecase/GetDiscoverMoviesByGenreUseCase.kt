package com.muasdev.moviedb_android.domain.usecase

import com.muasdev.moviedb_android.domain.repository.MovieRepository
import javax.inject.Inject

class GetDiscoverMoviesByGenreUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(genreId: String?) = repository.getDiscoverMovieByGenre(genreId)
}