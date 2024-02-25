package com.muasdev.moviedb_android.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muasdev.moviedb_android.data.mapper.ResultMapperDtoToModel
import com.muasdev.moviedb_android.data.mapper.ResultMoviesMapperToDomain
import com.muasdev.moviedb_android.data.remote.api.MovieDbApiServices
import com.muasdev.moviedb_android.domain.model.discover.Result
import java.io.IOException
import kotlinx.coroutines.delay
import retrofit2.HttpException

const val STARTING_PAGE_INDEX = 1
private const val NETWORK_REQUEST_DELAY_MILLIS = 2000L

class PageKeyedDiscoverMoviePagingSource(
    private val apiServices: MovieDbApiServices,
    private val genreId: String?
) : PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val nextPage = params.key ?: STARTING_PAGE_INDEX
            val response = apiServices.getPagingDiscoverMovieByGenreFromRemote(
                page = nextPage,
                withGenres = genreId
            )
            delay(NETWORK_REQUEST_DELAY_MILLIS)
            val resultModel = response.results.map { resultDto ->
                resultDto.let { result ->
                    ResultMapperDtoToModel().mapFrom(
                        result
                    )
                }
            }
            val resultDomain = resultModel.map { result ->
                ResultMoviesMapperToDomain().mapFrom(result)
            }
            LoadResult.Page(
                data = resultDomain,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (resultDomain.isEmpty()) null else response.page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}