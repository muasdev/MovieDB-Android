package com.muasdev.moviedb_android.data.remote.dto.movie_reviews


import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    @SerializedName("avatar_path")
    val avatarPath: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("rating")
    val rating: Int? = null,
    @SerializedName("username")
    val username: String? = null
)