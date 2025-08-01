package com.ipekkochisarli.obssmovies.features.movie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.ipekkochisarli.obssmovies.common.CarouselItem
import com.ipekkochisarli.obssmovies.databinding.ItemCarouselMovieBinding

class CarouselPagerAdapter(
    private var items: List<CarouselItem>,
) : RecyclerView.Adapter<CarouselPagerAdapter.CarouselViewHolder>() {
    inner class CarouselViewHolder(
        private val binding: ItemCarouselMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarouselItem) {
            binding.apply {
                imagePoster.load(item.imageUrl)
                tvTitle.text = item.title
                tvReleaseDate.text = item.releaseYear
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CarouselViewHolder {
        val binding =
            ItemCarouselMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CarouselViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CarouselItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
