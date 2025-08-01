package com.ipekkochisarli.obssmovies.features.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.databinding.FragmentHomeBinding
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.CarouselPagerAdapter
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.CategorySectionAdapter
import com.ipekkochisarli.obssmovies.features.home.ui.mapper.toCarouselItems
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var categoryAdapter: CategorySectionAdapter
    private lateinit var carouselAdapter: CarouselPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCarousel()
        collectUiState()

        viewModel.loadMovies()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategorySectionAdapter(emptyList())
        binding.recyclerViewSections.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupCarousel() {
        carouselAdapter = CarouselPagerAdapter(emptyList())
        binding.viewPagerCarousel.adapter = carouselAdapter
        binding.dotsIndicator.attachTo(binding.viewPagerCarousel)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.uiStates.collect { states ->
                    val nowPlayingSection = states.find { it.type == HomeSectionType.NOW_PLAYING }
                    val carouselItems = nowPlayingSection?.movies?.toCarouselItems()
                    carouselItems?.let { carouselAdapter.updateItems(it) }

                    categoryAdapter.updateSections(states)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
