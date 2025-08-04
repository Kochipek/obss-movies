package com.ipekkochisarli.obssmovies.features.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentHomeBinding
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.ui.HomeViewModel
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.CarouselPagerAdapter
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.CategorySectionAdapter
import com.ipekkochisarli.obssmovies.features.home.ui.mapper.toCarouselItems
import com.ipekkochisarli.obssmovies.features.movielist.MovieListFragmentData
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_LIST_DATA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var categoryAdapter: CategorySectionAdapter
    private lateinit var carouselAdapter: CarouselPagerAdapter

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
        categoryAdapter =
            CategorySectionAdapter(emptyList()) { sectionType ->
                onSeeAllClicked(sectionType)
            }
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

    private fun onSeeAllClicked(sectionType: HomeSectionType) {
        val section = viewModel.uiStates.value.find { it.type == sectionType }
        val header = getString(sectionType.titleRes)

        val moviesList = section?.movies ?: emptyList()
        val data =
            MovieListFragmentData(
                header = header,
                movieList = moviesList,
            )

        val bundle =
            Bundle().apply {
                putParcelable(MOVIE_LIST_DATA, data)
            }

        findNavController().navigate(
            R.id.action_homeFragment_to_movieListFragment,
            bundle,
        )
    }
}
