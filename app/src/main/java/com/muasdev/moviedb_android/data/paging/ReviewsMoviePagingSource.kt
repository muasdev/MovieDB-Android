package com.muasdev.moviedb_android.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muasdev.moviedb_android.data.mapper.asDataModel
import com.muasdev.moviedb_android.data.mapper.asDomainModel
import com.muasdev.moviedb_android.data.remote.api.MovieDbApiServices
import com.muasdev.moviedb_android.domain.model.movie_reviews.MovieReviewResults
import java.io.IOException
import kotlinx.coroutines.delay
import retrofit2.HttpException

const val START_PAGE_INDEX = 1
private const val NETWORK_REQUEST_DELAY_MILLIS = 2000L

class ReviewsMoviePagingSource(
    private val apiServices: MovieDbApiServices,
    private val movieId: Int
) : PagingSource<Int, MovieReviewResults>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieReviewResults> {
        return try {
            val nextPage = params.key ?: START_PAGE_INDEX
            val response = apiServices.getMovieReviews(
                page = nextPage,
                movieId = movieId
            )
            delay(NETWORK_REQUEST_DELAY_MILLIS)
            val resultModel = response.results.map { resultDto ->
                resultDto.asDataModel()
            }
            val resultDomain = resultModel.map { result ->
                result.asDomainModel()
            }
            LoadResult.Page(
                data = resultDomain,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.totalPages == nextPage || resultDomain.isEmpty()) null else response.page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieReviewResults>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}