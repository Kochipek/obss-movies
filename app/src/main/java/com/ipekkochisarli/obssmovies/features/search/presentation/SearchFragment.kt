package com.ipekkochisarli.obssmovies.features.search.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.CustomLoadingDialog
import com.ipekkochisarli.obssmovies.common.ErrorDialog
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentSearchBinding
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import com.ipekkochisarli.obssmovies.util.extensions.updateLayoutManagerWithState
import com.ipekkochisarli.obssmovies.util.extensions.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: MovieListAdapter

    private val loadingDialog: CustomLoadingDialog by lazy {
        CustomLoadingDialog(requireContext())
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView()
        setupSearchView()
        observeUiState()
        setupNavigation()
    }

    private fun setupAdapter() {
        adapter =
            MovieListAdapter(
                viewType = viewModel.uiState.value.viewType,
                onMovieClick = { navigateToContentDetail(it.id) },
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
                showFavoriteIcon = true,
            )
        binding.rvSearchList.adapter = adapter
    }

    private fun setupRecyclerView() {
        binding.rvSearchList.layoutManager = LinearLayoutManager(requireContext())

        binding.buttonToggleView.setOnClickListener {
            viewModel.toggleViewType()
        }
    }

    private fun setupSearchView() =
        with(binding.csvSearch) {
            onQueryTextChangeListener = { viewModel.search(it.trim()) }
            onSearchActionListener = { viewModel.search(it.trim()) }
            onCancelClickListener = { viewModel.loadPopularMovies() }
        }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->

                    loadingDialog.showLoading(state.isLoading)

                    if (state.error != null) {
                        ErrorDialog.show(parentFragmentManager, state.error)
                    }

                    updateAdapter(state.results)
                    updateViewType(state.viewType)
                    updateEmptyState(state.results.isEmpty())
                }
            }
        }
    }

    private fun updateTitle(query: String) {
        binding.tvSearchTitle.text =
            if (query.isBlank()) {
                getString(R.string.section_popular)
            } else {
                getString(R.string.search_results)
            }
    }

    private fun updateAdapter(results: List<MovieUiModel>) {
        adapter.submitList(results)
    }

    private fun updateViewType(viewType: MovieViewType) {
        binding.rvSearchList.updateLayoutManagerWithState(viewType, binding.buttonToggleView)
        adapter.setViewType(viewType)
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.llEmptyList.visibleIf(isEmpty)
        binding.rvSearchList.visibleIf(!isEmpty)
    }

    private fun setupNavigation() {
        binding.btnBacktoHome.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    private fun navigateToContentDetail(movieId: Int) {
        findNavController().navigate(
            R.id.action_searchFragment_to_contentDetailFragment,
            Bundle().apply { putInt(MOVIE_ID, movieId) },
        )
    }
}
