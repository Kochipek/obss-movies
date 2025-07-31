package com.ipekkochisarli.obssmovies.features.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.databinding.ItemOnboardingBinding
import com.ipekkochisarli.obssmovies.features.onboarding.uimodel.OnBoardingUiModel

class OnBoardingPagerAdapter(
    private val onBoardingPageData: List<OnBoardingUiModel>,
    private val onButtonClick: () -> Unit,
) : RecyclerView.Adapter<OnBoardingPagerAdapter.OnboardingViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OnboardingViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OnboardingViewHolder,
        position: Int,
    ) {
        val page = onBoardingPageData[position]
        holder.bind(page)
    }

    override fun getItemCount(): Int = onBoardingPageData.size

    inner class OnboardingViewHolder(
        private val binding: ItemOnboardingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(page: OnBoardingUiModel) {
            binding.title.text = itemView.context.getString(page.titleResId)
            binding.description.text = itemView.context.getString(page.descriptionResId)
            binding.image.setImageResource(page.imageResId)

            if (bindingAdapterPosition == onBoardingPageData.size - 1) {
                binding.buttonNext.text = itemView.context.getString(R.string.get_started)
            } else {
                binding.buttonNext.text = itemView.context.getString(R.string.next)
            }

            binding.buttonNext.setOnClickListener {
                onButtonClick()
            }
        }
    }
}
