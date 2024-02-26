package com.muasdev.moviedb_android.data.remote.dto.movie_videos

import com.google.gson.annotations.SerializedName

data class MovieVideosDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("results")
    val results: List<Result?>? = null
)