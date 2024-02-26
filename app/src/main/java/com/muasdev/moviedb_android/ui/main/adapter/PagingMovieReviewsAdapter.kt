package com.muasdev.moviedb_android.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.muasdev.moviedb_android.databinding.ItemListReviewsBinding
import com.muasdev.moviedb_android.domain.model.movie_reviews.MovieReviewResults
import com.muasdev.moviedb_android.utils.Constant

class PagingMovieReviewsAdapter:
    PagingDataAdapter<MovieReviewResults, PagingMovieReviewsAdapter.EpisodeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeViewHolder(
            ItemListReviewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val items = getItem(position)
        items?.let { item ->
            holder.bind(item)
        }
    }

    inner class EpisodeViewHolder(private val binding: ItemListReviewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieReviewResults) = with(binding) {
            tvAuthor.text = item.author
            tvContent.text = item.content
            ivAuthor.load("${Constant.IMAGE_URL}/${item.avatarPath}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            VIEW_TYPE_FOOTER_ITEM
        } else {
            VIEW_TYPE_CONTENT_ITEM
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<MovieReviewResults>() {
        override fun areItemsTheSame(oldItem: MovieReviewResults, newItem: MovieReviewResults) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieReviewResults, newItem: MovieReviewResults) =
            oldItem == newItem
    }

    companion object {
        const val VIEW_TYPE_CONTENT_ITEM = 1
        const val VIEW_TYPE_FOOTER_ITEM = 0
    }
}