package com.ipekkochisarli.obssmovies.features.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.CustomLoadingDialog
import com.ipekkochisarli.obssmovies.common.ErrorDialog
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentHomeBinding
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.CarouselPagerAdapter
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.CategorySectionAdapter
import com.ipekkochisarli.obssmovies.features.home.ui.mapper.toCarouselItems
import com.ipekkochisarli.obssmovies.features.movielist.MovieListFragmentData
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_LIST_DATA
import com.ipekkochisarli.obssmovies.util.extensions.gone
import com.ipekkochisarli.obssmovies.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var categoryAdapter: CategorySectionAdapter
    private lateinit var carouselAdapter: CarouselPagerAdapter

    private val loadingDialog: CustomLoadingDialog by lazy {
        CustomLoadingDialog(requireContext())
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
        categoryAdapter =
            CategorySectionAdapter(
                showFavoriteIcon = true,
                onSeeAllClick = { sectionType ->
                    onSeeAllClicked(sectionType)
                },
                onMovieClick = { movie ->
                    navigateToContentDetail(movie.id)
                },
                onFavoriteClick = { movie ->
                    if (viewModel.isGuest) {
                        ErrorDialog.show(
                            parentFragmentManager,
                            getString(R.string.login_required_message),
                        )
                    } else {
                        viewModel.toggleWatchlist(movie.id)
                    }
                },
            )
        binding.recyclerViewSections.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
            setHasFixedSize(true)
        }
        binding.imageViewProfileIcon.setOnClickListener {
            navigateToProfile()
        }

        if (!viewModel.isGuest) {
            binding.imageViewProfileIcon.visible()
        } else {
            binding.imageViewProfileIcon.gone()
        }
    }

    private fun setupCarousel() {
        carouselAdapter =
            CarouselPagerAdapter(emptyList()) { carouselItem ->
                navigateToContentDetail(carouselItem.id)
            }
        binding.viewPagerCarousel.adapter = carouselAdapter
        binding.dotsIndicator.attachTo(binding.viewPagerCarousel)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStates.collect { states ->

                    val isLoading = states.any { it.isLoading }
                    loadingDialog.showLoading(isLoading)

                    val nowPlayingSection = states.find { it.type == HomeSectionType.NOW_PLAYING }
                    val carouselItems = nowPlayingSection?.movies?.toCarouselItems()
                    carouselItems?.let { carouselAdapter.updateItems(it) }

                    categoryAdapter.submitList(states)

                    val firstErrorState = states.firstOrNull { it.error != null }
                    firstErrorState?.let { showErrorDialog(it) }
                }
            }
        }
    }

    private fun showErrorDialog(uiState: HomeUiState) {
        uiState.error?.let { errorMessage ->
            ErrorDialog(
                message = errorMessage,
                onButtonClick = { viewModel.loadMovies() },
            ).show(parentFragmentManager, "ErrorDialog")
        }
    }

    private fun navigateToContentDetail(movieId: Int) {
        val bundle =
            Bundle().apply {
                putInt(MOVIE_ID, movieId)
            }
        findNavController().navigate(
            R.id.action_homeFragment_to_contentDetailFragment,
            bundle,
        )
    }

    private fun navigateToProfile() {
        findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
    }

    private fun onSeeAllClicked(sectionType: HomeSectionType) {
        val bundle =
            Bundle().apply {
                putSerializable("sectionType", sectionType)
            }
        findNavController().navigate(
            R.id.action_homeFragment_to_movieListFragment,
            bundle,
        )
    }
}
