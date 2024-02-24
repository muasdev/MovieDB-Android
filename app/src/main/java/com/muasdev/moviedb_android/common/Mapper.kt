package com.muasdev.moviedb_android.common

interface Mapper<F,T> {
    fun mapFrom(from:F):T
}