package com.muasdev.moviedb_android.data.remote.dto.discover

import com.google.gson.annotations.SerializedName

data class DiscoverMoviesDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultDto>,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)