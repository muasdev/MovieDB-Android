package com.muasdev.moviedb_android.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.muasdev.moviedb_android.databinding.ItemListDiscoverMoviesBinding
import com.muasdev.moviedb_android.domain.model.discover.Result
import com.muasdev.moviedb_android.utils.Constant

class PagingDiscoverMoviesAdapter(
    private val onItemClicked: (Int?) -> Unit
):
    PagingDataAdapter<Result, PagingDiscoverMoviesAdapter.EpisodeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeViewHolder(
            ItemListDiscoverMoviesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val items = getItem(position)
        items?.let { item ->
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemClicked(item.id)
            }
        }
    }

    inner class EpisodeViewHolder(private val binding: ItemListDiscoverMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Result) = with(binding) {
            tvTitle.text = item.title
            ivMovie.load("${Constant.IMAGE_URL}/${item.posterPath}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            VIEW_TYPE_FOOTER_ITEM
        } else {
            VIEW_TYPE_CONTENT_ITEM
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result) =
            oldItem == newItem
    }

    companion object {
        const val VIEW_TYPE_CONTENT_ITEM = 1
        const val VIEW_TYPE_FOOTER_ITEM = 0
        const val SPAN_SIZE_CONTENT_ITEM = 1
        const val SPAN_SIZE_FOOTER_ITEM = 2
    }
}