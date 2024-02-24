package com.muasdev.moviedb_android.data.remote.utils

import com.google.gson.Gson
import retrofit2.HttpException

fun HttpException.handleHttpException(): String {
    val errorBody = this.response()?.errorBody()?.string()
    val errorMessage = try {
        Gson().fromJson(errorBody, ErrorResponse::class.java)?.statusMessage
    } catch (_: Exception) {
        "Oops!. Something went wrong"
    }
    return errorMessage ?: "Oops!. Something went wrong"
}

data class ErrorResponse(
    val statusCode: Int,
    val statusMessage: String
)