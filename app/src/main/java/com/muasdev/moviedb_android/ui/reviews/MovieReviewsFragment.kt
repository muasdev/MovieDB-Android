package com.muasdev.moviedb_android.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.muasdev.moviedb_android.databinding.FragmentMovieReviewsBinding
import com.muasdev.moviedb_android.domain.model.movie_reviews.MovieReviewResults
import com.muasdev.moviedb_android.ui.main.adapter.PagingLoadStateAdapter
import com.muasdev.moviedb_android.ui.main.adapter.PagingMovieReviewsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieReviewsFragment : Fragment() {

    private var _binding: FragmentMovieReviewsBinding? = null
    private val binding get() = _binding!!

    private val args: MovieReviewsFragmentArgs by navArgs()

    private val viewModel: MovieReviewsViewModel by activityViewModels()
    private lateinit var pagingAdapter: PagingMovieReviewsAdapter


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = args.movieId

        viewModel.onEvent(MovieReviewsEvent.LoadMovieReviews(movieId))

        pagingAdapter = PagingMovieReviewsAdapter()

        observeState()
        observePagingLoadState()
    }

    private fun observePagingLoadState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                pagingAdapter.loadStateFlow.collectLatest { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        pagingAdapter.itemCount < 1
                    ) {
                        binding.rvReviews.isVisible = false
                        showMovieEmptySection()
                    } else {
                        hideMovieEmptySection()
                        binding.rvReviews.isVisible = true
                    }
                }
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    if (state.reviews != null) {
                        handleMovieReviews(state.reviews)
                    }
                    if (state.isLoading) {
                        binding.apply {
                            rvReviews.isVisible = false
                            pbLoader.isVisible = true
                            hideMovieEmptySection()
                        }
                    }
                    if (!state.errorMessage.isNullOrEmpty()) {
                        binding.apply {
                            pbLoader.isVisible = false
                            rvReviews.isVisible = false
                            hideMovieEmptySection()
                        }
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private suspend fun handleMovieReviews(movieReviews: PagingData<MovieReviewResults>) {
        binding.apply {
            pbLoader.isVisible = false
            tvReviewsEmpty.isVisible = false
            rvReviews.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvReviews.adapter = pagingAdapter
        }
        with(pagingAdapter) {
            binding.rvReviews.adapter = withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(this),
                footer = PagingLoadStateAdapter(this),
            )
            submitData(movieReviews)
        }
    }

    private fun hideMovieEmptySection() {
        binding.apply {
            tvReviewsEmpty.isVisible = false
        }
    }

    private fun showMovieEmptySection() {
        binding.apply {
            tvReviewsEmpty.isVisible = true
        }
    }

}