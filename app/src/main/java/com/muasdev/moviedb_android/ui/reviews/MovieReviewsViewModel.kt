package com.muasdev.moviedb_android.ui.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.muasdev.moviedb_android.domain.model.movie_reviews.MovieReviewResults
import com.muasdev.moviedb_android.domain.usecase.GetMovieReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class MovieReviewsViewModel @Inject constructor(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase
): ViewModel() {

    private val _state = MutableStateFlow(MovieReviewsState())
    val state: StateFlow<MovieReviewsState> = _state

    fun onEvent(event: MovieReviewsEvent) {
        when(event) {
            is MovieReviewsEvent.LoadMovieReviews -> {
                getPagingMovieReviews(event.movieId)
            }
        }
    }

    private fun getPagingMovieReviews(movieId: Int) {
        viewModelScope.launch {
            handleDiscoverMoviesLoading()
            delay(DELAY_DURATION_MILLIS)
            getMovieReviewsUseCase.invoke(movieId, viewModelScope)
                .onEach { data ->
                    handleDiscoverMoviesSuccess(data)
                }
                .catch {
                    handleDiscoverMoviesError(it.message)
                }.launchIn(viewModelScope)
        }
    }

    private fun handleDiscoverMoviesLoading() {
        _state.value = MovieReviewsState(
            reviews = null,
            isLoading = true,
            errorMessage = null,
        )
    }

    private fun handleDiscoverMoviesSuccess(data: PagingData<MovieReviewResults>) {
        _state.value = MovieReviewsState(
            reviews = data,
            isLoading = false,
            errorMessage = null,
        )
    }

    private fun handleDiscoverMoviesError(errorMessage: String?) {
        _state.value = state.value.copy(
            isLoading = false,
            errorMessage = errorMessage
        )
    }

    companion object {
        private const val DELAY_DURATION_MILLIS = 700L
    }
}