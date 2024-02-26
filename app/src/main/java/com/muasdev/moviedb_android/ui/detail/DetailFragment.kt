package com.muasdev.moviedb_android.ui.detail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.muasdev.moviedb_android.databinding.FragmentDetailBinding
import com.muasdev.moviedb_android.domain.model.detail_movie.MovieDetails
import com.muasdev.moviedb_android.domain.model.detail_movie.MovieTrailer
import com.muasdev.moviedb_android.utils.Constant
import com.muasdev.moviedb_android.utils.DateConverter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by activityViewModels()

    private val args: DetailFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId
        viewModel.onEvent(DetailEvent.LoadMovieDetails(movieId))
        observeState()

        binding.apply {
            tvReviewsTitle.setOnClickListener {
                val action =
                    DetailFragmentDirections.actionDetailFragmentToMovieReviewsFragment(
                        movieId = movieId
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    if (state.movieDetails != null) {
                        handleMovieDetails(state.movieDetails)
                    }
                    if (state.movieTrailer != null) {
                        handleMovieTrailer(state.movieTrailer)
                    }
                    if (!state.errorMessage.isNullOrEmpty()) {
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun handleMovieTrailer(data: MovieTrailer) {
        val videoId = data.key
        val imgUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
        binding.apply {
            ivTrailerMovie.load(imgUrl)
            ivPlayButton.isVisible = true
            ivTrailerMovie.setOnClickListener {
                context?.let { ctx -> watchYoutubeVideo(ctx, videoId) }
            }
        }
    }

    private fun handleMovieDetails(data: MovieDetails) {
        binding.apply {
            ivBackdrop.load("${Constant.IMAGE_URL}/${data.backdropPath}")
            ivPoster.load("${Constant.IMAGE_URL}/${data.posterPath}")
            tvTitle.text = "Title : \n${data.title}"
            tvStatus.text = "Status : \n${data.status}"
            tvOverview.text = data.overview
            tvReleaseDate.text = "Release Date : \n${data.releaseDate?.let { DateConverter.convertDate(it) }}"
        }
    }

    private fun watchYoutubeVideo(context: Context, videoId: String?) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$videoId")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }

}