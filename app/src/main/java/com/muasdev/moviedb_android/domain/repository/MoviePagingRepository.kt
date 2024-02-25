package com.muasdev.moviedb_android.domain.repository

import androidx.paging.PagingData
import com.muasdev.moviedb_android.domain.model.discover.Result
import kotlinx.coroutines.flow.Flow

interface MoviePagingRepository {
    suspend fun getPagingDiscoverMovieByGenre(page: Int?, genreId: String?): Flow<PagingData<Result>>
}