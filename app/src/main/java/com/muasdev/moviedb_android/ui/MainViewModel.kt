package com.muasdev.moviedb_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muasdev.moviedb_android.data.Resource
import com.muasdev.moviedb_android.domain.usecase.GetAllGenresForMoviesUseCase
import com.muasdev.moviedb_android.domain.usecase.GetDiscoverMoviesByGenreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllGenresForMovies: GetAllGenresForMoviesUseCase,
    private val getDiscoverMoviesByGenreUseCase: GetDiscoverMoviesByGenreUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.FilterMoviesByGenre -> {
                getDiscoverMovieByGenre(genreId = event.genreId)
            }
        }
    }

    init {
        getAllGenresForMovie()
        getDiscoverMovieByGenre()
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

    private fun getDiscoverMovieByGenre(genreId: String? = null) {
        viewModelScope.launch {
            getDiscoverMoviesByGenreUseCase(genreId).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isDiscoverLoading = true,
                            errorMessage = null,
                        )
                    }
                    is Resource.Success -> {
                        _state.value = MainState(
                            discoverMovies = result.data,
                            isDiscoverLoading = false,
                            errorMessage = null,
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isDiscoverLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}