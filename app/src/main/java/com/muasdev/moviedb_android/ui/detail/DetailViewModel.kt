package com.muasdev.moviedb_android.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muasdev.moviedb_android.data.Resource
import com.muasdev.moviedb_android.domain.model.detail_movie.MovieTrailer
import com.muasdev.moviedb_android.domain.usecase.GetMovieDetailsUseCase
import com.muasdev.moviedb_android.domain.usecase.GetMovieVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase
): ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    fun onEvent(event: DetailEvent) {
        when(event) {
            is DetailEvent.LoadMovieDetails -> {
                getMovieDetails(event.movieId)
            }
            is DetailEvent.NavigateToReviewsPage -> {
                _state.value = state.value.copy(
                    isLoading = true
                )
            }
        }
    }

    private fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val movieDetails = getMovieDetailsUseCase(movieId)
            movieDetails.onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true,
                            errorMessage = null,
                        )
                    }
                    is Resource.Success -> {
                        _state.value = DetailState(
                            isLoading = false,
                            movieDetails = result.data,
                            errorMessage = null
                        )
                        getMovieVideos(movieId)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun getMovieVideos(movieId: Int) {
        viewModelScope.launch {
            val movieVideos = getMovieVideosUseCase(movieId)
            movieVideos.onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true,
                            errorMessage = null,
                        )
                    }
                    is Resource.Success -> {
                        val trailers: List<MovieTrailer> = result.data?.results
                            ?.filter { it?.type == "Trailer" }
                            ?.map {
                                MovieTrailer(
                                    id = it?.id,
                                    name = it?.name,
                                    key = it?.key,
                                )
                            } ?: emptyList()
                        val movieTrailer: MovieTrailer? = if (trailers.isNotEmpty()) trailers[0] else null
                        Timber.d("trailers: $trailers")
                        _state.value = DetailState(
                            isLoading = false,
                            movieTrailer = movieTrailer,
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}