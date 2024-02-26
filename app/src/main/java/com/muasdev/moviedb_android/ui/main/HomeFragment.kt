package com.muasdev.moviedb_android.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.muasdev.moviedb_android.R
import com.muasdev.moviedb_android.databinding.FragmentHomeBinding
import com.muasdev.moviedb_android.domain.model.discover.Result
import com.muasdev.moviedb_android.domain.model.genres.Genre
import com.muasdev.moviedb_android.domain.model.genres.Genres
import com.muasdev.moviedb_android.ui.main.adapter.GenresAdapter
import com.muasdev.moviedb_android.ui.main.adapter.PagingDiscoverMoviesAdapter
import com.muasdev.moviedb_android.ui.main.adapter.PagingLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var pagingAdapter: PagingDiscoverMoviesAdapter
    private lateinit var genresAdapter: GenresAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        pagingAdapter = PagingDiscoverMoviesAdapter {
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
        viewModel.getAllGenresForMovie()
        genresAdapter = GenresAdapter { genre, _ ->
            viewModel.onEvent(
                MainEvent.FilterMoviesByGenre(
                    genreId = genre.id?.let { id -> if (id == 0) null else "$id" }
                )
            )
        }
        binding.apply {
            rvGenres.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvGenres.adapter = genresAdapter
        }

        observeState()
        observePagingLoadState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    if (state.discoverMoviesPaging != null) {
                        handleDiscoverMovie(state.discoverMoviesPaging)
                    }
                    if (state.isDiscoverLoading) {
                        binding.apply {
                            rvDiscoverMovie.isVisible = false
                            pbLoader.isVisible = true
                            hideMovieEmptySection()
                        }
                    }
                    if (state.genres != null) {
                        handleGenres(state.genres)
                    }
                    if (!state.errorMessage.isNullOrEmpty()) {
                        binding.apply {
                            pbLoader.isVisible = false
                            rvDiscoverMovie.isVisible = false
                            hideMovieEmptySection()
                        }
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun handleGenres(genres: Genres) {
        val allGenresItem = Genre(id = 0, name = "All Genre")
        val filteredGenres = genres.genres.orEmpty().filterNotNull()
        val updatedGenresList = mutableListOf(allGenresItem)
        updatedGenresList.addAll(filteredGenres)
        genresAdapter.submitList(updatedGenresList)
    }

    private suspend fun handleDiscoverMovie(discoverMoviesPaging: PagingData<Result>) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = pagingAdapter.getItemViewType(position)
                return if (viewType == PagingDiscoverMoviesAdapter.VIEW_TYPE_CONTENT_ITEM) {
                    PagingDiscoverMoviesAdapter.SPAN_SIZE_CONTENT_ITEM
                } else {
                    PagingDiscoverMoviesAdapter.SPAN_SIZE_FOOTER_ITEM
                }
            }
        }
        binding.apply {
            pbLoader.isVisible = false
            tvMovieEmpty.isVisible = false
            rvDiscoverMovie.apply {
                this.layoutManager = gridLayoutManager
                this.setHasFixedSize(true)
                this.adapter = pagingAdapter
            }
        }
        with(pagingAdapter) {
            binding.rvDiscoverMovie.adapter = withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(this),
                footer = PagingLoadStateAdapter(this),
            )
            submitData(discoverMoviesPaging)
        }
    }

    private fun observePagingLoadState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                pagingAdapter.loadStateFlow.collectLatest { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        pagingAdapter.itemCount < 1
                    ) {
                        binding.rvDiscoverMovie.isVisible = false
                        showMovieEmptySection()
                    } else {
                        hideMovieEmptySection()
                        binding.rvDiscoverMovie.isVisible = true
                    }
                }
            }
        }
    }

    private fun hideMovieEmptySection() {
        binding.apply {
            ivMovieEmpty.isVisible = false
            tvMovieEmpty.isVisible = false
        }
    }

    private fun showMovieEmptySection() {
        binding.apply {
            ivMovieEmpty.isVisible = true
            tvMovieEmpty.isVisible = true
        }
    }
}