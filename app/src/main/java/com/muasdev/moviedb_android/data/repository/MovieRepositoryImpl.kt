package com.muasdev.moviedb_android.data.repository

import com.muasdev.moviedb_android.data.Resource
import com.muasdev.moviedb_android.data.mapper.GenresMapperToDomain
import com.muasdev.moviedb_android.data.remote.RemoteMoviesSource
import com.muasdev.moviedb_android.data.remote.utils.handleHttpException
import com.muasdev.moviedb_android.domain.model.genres.Genres
import com.muasdev.moviedb_android.domain.repository.MovieRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class MovieRepositoryImpl @Inject constructor(
    private val remoteMoviesSource: RemoteMoviesSource
): MovieRepository {

    override suspend fun getAllGenresForMovie(): Flow<Resource<Genres>> = flow {
        emit(Resource.Loading())
        try {
            val response = remoteMoviesSource.getAllGenresForMovie()
            emit(Resource.Success(GenresMapperToDomain().mapFrom(response)))
        } catch (e: HttpException) {
            val errorMessage = e.handleHttpException()
            emit(Resource.Error(message = errorMessage))
        } catch (e: IOException) {
            emit(
                Resource.Error(message = "Oops!. check your internet connection.")
            )
        }
    }

}