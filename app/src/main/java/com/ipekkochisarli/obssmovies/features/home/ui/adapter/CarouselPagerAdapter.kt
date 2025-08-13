package com.ipekkochisarli.obssmovies.features.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.common.CarouselItem
import com.ipekkochisarli.obssmovies.databinding.ItemCarouselMovieBinding
import com.ipekkochisarli.obssmovies.util.extensions.loadImage

class CarouselPagerAdapter(
    private var items: List<CarouselItem>,
    private val onItemClick: ((CarouselItem) -> Unit)? = null,
) : RecyclerView.Adapter<CarouselPagerAdapter.CarouselViewHolder>() {
    inner class CarouselViewHolder(
        private val binding: ItemCarouselMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarouselItem) {
            binding.apply {
                imagePoster.loadImage(item.imageUrl)
                tvTitle.text = item.title
                tvReleaseDate.text = item.releaseYear

                binding.root.setOnClickListener {
                    onItemClick?.invoke(item)
                }
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
