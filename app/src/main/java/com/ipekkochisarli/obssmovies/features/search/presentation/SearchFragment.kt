package com.ipekkochisarli.obssmovies.features.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.databinding.FragmentSearchBinding
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
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
        setupSearchView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                adapter.updateMovies(uiState.results)

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
            }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob =
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000)
                viewModel.search(query)
            }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (it.isNotBlank()) {
                            viewModel.search(it.trim())
                        }
                    }
                    binding.searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        if (it.isNotBlank()) {
                            search(it.trim())
                        } else {
                            viewModel.clearSearchResults()
                        }
                    }
                    return false
                }
            },
        )
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
