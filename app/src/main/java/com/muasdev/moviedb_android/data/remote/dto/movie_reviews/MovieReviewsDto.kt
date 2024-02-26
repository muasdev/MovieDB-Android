package com.muasdev.moviedb_android.data.remote.dto.movie_reviews

import com.google.gson.annotations.SerializedName

data class MovieReviewsDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: List<Result?>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)