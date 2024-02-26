package com.muasdev.moviedb_android.ui.reviews

sealed class MovieReviewsEvent {
    data class LoadMovieReviews(
        val movieId: Int
    ): MovieReviewsEvent()
}