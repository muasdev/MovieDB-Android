package com.muasdev.moviedb_android.data.model.genres

data class Genres(
    val genres: List<Genre?>? = null
)

data class Genre(
    val id: Int? = null,
    val name: String? = null
)