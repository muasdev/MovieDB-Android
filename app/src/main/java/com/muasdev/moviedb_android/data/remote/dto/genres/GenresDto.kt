package com.muasdev.moviedb_android.data.remote.dto.genres

import com.google.gson.annotations.SerializedName

data class GenresDto(
    @SerializedName("genres")
    val genres: List<GenreDto?>? = null
)