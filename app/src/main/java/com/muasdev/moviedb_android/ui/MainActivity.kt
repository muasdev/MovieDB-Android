package com.muasdev.moviedb_android.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.muasdev.moviedb_android.databinding.ActivityMainBinding
import com.muasdev.moviedb_android.domain.model.discover.Result
import com.muasdev.moviedb_android.ui.adapter.PagingDiscoverMoviesAdapter
import com.muasdev.moviedb_android.ui.adapter.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    lateinit var pagingAdapter: PagingDiscoverMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val listGenre = listOf("28", "15", "50", "123")

        binding.apply {
            btnTest.setOnClickListener {
                viewModel.onEvent(MainEvent.FilterMoviesByGenre(listGenre.randomOrNull()))
            }
        }

        pagingAdapter = PagingDiscoverMoviesAdapter()

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
                    if (state.isGenresLoading) {
                        //
                    }
                    if (state.isDiscoverLoading) {
                        binding.apply {
                            rvDiscoverMovie.isVisible = false
                            pbLoader.isVisible = true
                            hideMovieEmptySection()
                        }
                    }
                    if (state.genres != null) {
                        //
                    }
                    if (!state.errorMessage.isNullOrEmpty()) {
                        binding.apply {
                            pbLoader.isVisible = false
                            rvDiscoverMovie.isVisible = false
                            hideMovieEmptySection()
                        }
                        Toast.makeText(this@MainActivity, state.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private suspend fun handleDiscoverMovie(discoverMoviesPaging: PagingData<Result>) {
        val gridLayoutManager = GridLayoutManager(this@MainActivity, 2)
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