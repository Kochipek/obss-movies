package com.ipekkochisarli.obssmovies.features.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.databinding.FragmentSearchBinding
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import com.ipekkochisarli.obssmovies.util.extensions.gone
import com.ipekkochisarli.obssmovies.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: MovieListAdapter

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        setupAdapter()
        setupRecyclerView()
        setupToggleButton()
        setupCustomSearchView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                val data = uiState.results

                if (data.isNotEmpty()) {
                    adapter.updateMovies(data)
                }

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
                adapter.setViewType(uiState.viewType)

                if (uiState.query.isNotBlank() && data.isEmpty()) {
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
        adapter = MovieListAdapter(emptyList(), MovieViewType.LIST)
        binding.rvSearchList.adapter = adapter
    }

    private fun setupRecyclerView() {
        binding.rvSearchList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupToggleButton() {
        binding.buttonToggleView.setOnClickListener {
            viewModel.toggleViewType()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
