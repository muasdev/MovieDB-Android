package com.muasdev.moviedb_android.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.muasdev.moviedb_android.data.paging.PageKeyedDiscoverMoviePagingSource
import com.muasdev.moviedb_android.data.remote.api.MovieDbApiServices
import com.muasdev.moviedb_android.domain.model.discover.Result
import com.muasdev.moviedb_android.domain.repository.MoviePagingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

const val INITIAL_PAGE = 1
const val PAGE_SIZE = 10

class MoviePagingRepositoryImpl @Inject constructor(
    private val apiServices: MovieDbApiServices,
) : MoviePagingRepository {
    override suspend fun getPagingDiscoverMovieByGenre(
        page: Int?,
        genreId: String?,
    ): Flow<PagingData<Result>> = Pager(
        initialKey = INITIAL_PAGE,
        config = PagingConfig(PAGE_SIZE),
    ) {
        PageKeyedDiscoverMoviePagingSource(
            apiServices = apiServices,
            genreId = genreId
        )
    }.flow
}