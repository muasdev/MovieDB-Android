package com.muasdev.moviedb_android.ui.reviews

import androidx.paging.PagingData
import com.muasdev.moviedb_android.domain.model.movie_reviews.MovieReviewResults

data class MovieReviewsState (
    val isLoading: Boolean = false,
    val reviews: PagingData<MovieReviewResults>? = null,
    val errorMessage: String? = null,
)