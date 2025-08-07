package com.ipekkochisarli.obssmovies.features.search.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentSearchBinding
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import com.ipekkochisarli.obssmovies.util.extensions.gone
import com.ipekkochisarli.obssmovies.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: MovieListAdapter
    private var searchJob: Job? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        setupAdapter()
        setupRecyclerView()
        setupCustomSearchView()
        observeViewModel()
        navigateToHomeScreen()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                binding.tvSearchTitle.text =
                    if (uiState.query.isBlank()) {
                        getString(R.string.section_popular)
                    } else {
                        getString(R.string.search_results)
                    }

                adapter.submitList(uiState.results)
                adapter.setViewType(uiState.viewType)

                when (uiState.viewType) {
                    MovieViewType.LIST -> {
                        binding.rvSearchList.layoutManager = LinearLayoutManager(requireContext())
                        binding.buttonToggleView.setImageResource(R.drawable.ic_grid)
                    }

                    MovieViewType.GRID -> {
                        binding.rvSearchList.layoutManager = GridLayoutManager(requireContext(), 3)
                        binding.buttonToggleView.setImageResource(R.drawable.ic_list)
                    }

                    MovieViewType.POSTER -> {}
                }

                if (uiState.query.isNotBlank() && uiState.results.isEmpty()) {
                    binding.llEmptyList.visible()
                    binding.rvSearchList.gone()
                } else {
                    binding.llEmptyList.gone()
                    binding.rvSearchList.visible()
                }
            }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob =
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000) // 1 saniye debounce
                viewModel.search(query)
            }
    }

    private fun setupCustomSearchView() {
        with(binding.csvSearch) {
            onQueryTextChangeListener = { query ->
                if (query.isNotBlank()) {
                    search(query.trim())
                } else {
                    viewModel.clearSearchResults()
                }
            }

            onSearchActionListener = { query ->
                if (query.isNotBlank()) {
                    viewModel.search(query.trim())
                }
            }

            onCancelClickListener = {
                viewModel.clearSearchResults()
            }
        }
    }

    private fun setupAdapter() {
        adapter =
            MovieListAdapter(
                MovieViewType.LIST,
                onMovieClick = { movie ->
                    navigateToContentDetail(movie.id)
                },
            )
        binding.rvSearchList.adapter = adapter
    }

    private fun setupRecyclerView() {
        binding.rvSearchList.layoutManager = LinearLayoutManager(requireContext())
        setupToggleButton()
    }

    private fun setupToggleButton() {
        binding.buttonToggleView.setOnClickListener {
            viewModel.toggleViewType()
        }
    }

    private fun navigateToHomeScreen() {
        binding.btnBacktoHome.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    private fun navigateToContentDetail(movieId: Int) {
        val bundle =
            Bundle().apply {
                putInt(MOVIE_ID, movieId)
            }
        findNavController().navigate(
            R.id.action_searchFragment_to_contentDetailFragment,
            bundle,
        )
    }
}
