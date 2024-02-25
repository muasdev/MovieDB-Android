package com.muasdev.moviedb_android.ui.main.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muasdev.moviedb_android.databinding.ItemListGenreBinding
import com.muasdev.moviedb_android.domain.model.genres.Genre

class GenresAdapter(
    private val onItemClicked: (Genre, Int) -> Unit
): ListAdapter<Genre, GenresAdapter.ItemViewHolder>(DiffCallback) {

    private var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListGenreBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        val isSelected = position == selectedPosition
        holder.bind(current, isSelected)
        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.absoluteAdapterPosition
            notifyItemChanged(selectedPosition)
            onItemClicked(current, position)
        }
    }

    class ItemViewHolder(private var binding: ItemListGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Genre, isSelected: Boolean) {
            binding.tvNameGenre.text = item.name
            binding.tvNameGenre.isSelected = isSelected
            binding.tvNameGenre.setTextColor(
                if (isSelected) Color.parseColor("#FFFFFF") else Color.parseColor("#000000")
            )
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre) =
            oldItem == newItem
    }
}