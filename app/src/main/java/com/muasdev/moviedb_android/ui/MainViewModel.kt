package com.muasdev.moviedb_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.muasdev.moviedb_android.data.Resource
import com.muasdev.moviedb_android.domain.model.discover.Result
import com.muasdev.moviedb_android.domain.usecase.GetAllGenresForMoviesUseCase
import com.muasdev.moviedb_android.domain.usecase.GetDiscoverMoviesPagingByGenreUseCase
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
class MainViewModel @Inject constructor(
    private val getAllGenresForMovies: GetAllGenresForMoviesUseCase,
    private val getDiscoverMoviesByGenreUseCase: GetDiscoverMoviesPagingByGenreUseCase
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.FilterMoviesByGenre -> {
                getPagingDiscoverMovies(genreId = event.genreId)
            }
        }
    }

    init {
        getAllGenresForMovie()
    }

    private fun getAllGenresForMovie() {
        viewModelScope.launch {
            getAllGenresForMovies().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isGenresLoading = true,
                            errorMessage = null,
                        )
                    }
                    is Resource.Success -> {
                        _state.value = MainState(
                            genres = result.data,
                            isGenresLoading = false,
                            errorMessage = null,
                        )
                        getPagingDiscoverMovies()
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isGenresLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private fun getPagingDiscoverMovies(page: Int? = null, genreId: String? = null) {
        viewModelScope.launch {
            handleDiscoverMoviesLoading()
            delay(DELAY_DURATION_MILLIS)
            getDiscoverMoviesByGenreUseCase.invoke(page, genreId)
                .onEach { data ->
                    handleDiscoverMoviesSuccess(data)
                }
                .catch {
                    handleDiscoverMoviesError(it.message)
                }
                .launchIn(viewModelScope)
        }
    }

    private fun handleDiscoverMoviesLoading() {
        _state.value = MainState(
            discoverMoviesPaging = null,
            isDiscoverLoading = true,
            errorMessage = null,
        )
    }

    private fun handleDiscoverMoviesSuccess(data: PagingData<Result>) {
        _state.value = MainState(
            discoverMoviesPaging = data,
            isDiscoverLoading = false,
            errorMessage = null,
        )
    }

    private fun handleDiscoverMoviesError(errorMessage: String?) {
        _state.value = state.value.copy(
            isDiscoverLoading = false,
            errorMessage = errorMessage
        )
    }

    companion object {
        private const val DELAY_DURATION_MILLIS = 700L
    }
}