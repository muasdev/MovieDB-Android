package com.muasdev.moviedb_android.data.remote.dto.discover

import com.google.gson.annotations.SerializedName

data class DiscoverMoviesDto(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: List<ResultDto?>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)