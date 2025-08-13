package com.ipekkochisarli.obssmovies.features.search.presentation

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
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentSearchBinding
import com.ipekkochisarli.obssmovies.features.movielist.MoviePagingAdapter
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import com.ipekkochisarli.obssmovies.util.extensions.updateLayoutManagerWithState
import com.ipekkochisarli.obssmovies.util.extensions.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: MoviePagingAdapter

    private val loadingDialog: CustomLoadingDialog by lazy { CustomLoadingDialog(requireContext()) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeUiState()
        observePagingData()
    }

    private fun setupViews() {
        setupAdapter()
        setupRecyclerView()
        setupSearchView()
        setupNavigation()
    }

    private fun setupAdapter() {
        adapter =
            MoviePagingAdapter(
                viewType = viewModel.uiState.value.viewType,
                onMovieClick = { navigateToContentDetail(it.id) },
                showFavoriteIcon = false,
            )

        binding.rvSearchList.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            val isEmpty =
                loadState.refresh is androidx.paging.LoadState.NotLoading &&
                    adapter.itemCount == 0

            binding.llEmptyList.visibleIf(isEmpty)
            binding.rvSearchList.visibleIf(!isEmpty)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearchList.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonToggleView.setOnClickListener {
            viewModel.toggleViewType()
        }
    }

    private fun setupSearchView() =
        with(binding.csvSearch) {
            onQueryTextChangeListener = { query ->
                viewModel.setQuery(query.trim())
            }
            onSearchActionListener = { query ->
                viewModel.setQuery(query.trim())
            }
            onCancelClickListener = {
                viewModel.setQuery("")
            }
        }

    private fun setupNavigation() {
        binding.btnBacktoHome.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleLoadingState(state.isLoading)
                    handleErrorState(state.error)
                    handleViewTypeChange(state.viewType)
                    handleTitleUpdate(state.query)
                }
            }
        }
    }

    private fun observePagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    state.results?.collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
        loadingDialog.showLoading(isLoading)
    }

    private fun handleErrorState(error: String?) {
        error?.let {
            ErrorDialog.show(parentFragmentManager, it)
        }
    }

    private fun handleViewTypeChange(viewType: MovieViewType) {
        binding.rvSearchList.updateLayoutManagerWithState(viewType, binding.buttonToggleView)
        adapter.setViewType(viewType)
    }

    private fun handleTitleUpdate(query: String) {
        binding.tvSearchTitle.text =
            if (query.isBlank()) {
                getString(R.string.section_popular)
            } else {
                getString(R.string.search_results)
            }
    }

    private fun navigateToContentDetail(movieId: Int) {
        findNavController().navigate(
            R.id.action_searchFragment_to_contentDetailFragment,
            Bundle().apply { putInt(MOVIE_ID, movieId) },
        )
    }
}
