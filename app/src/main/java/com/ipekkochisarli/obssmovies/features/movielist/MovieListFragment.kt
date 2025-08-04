package com.ipekkochisarli.obssmovies.features.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.databinding.FragmentMovieListBinding
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieListAdapter
    private var currentViewType = MovieViewType.LIST

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieListAdapter(emptyList(), currentViewType)
        setupRecyclerView()

        val movies =
            arguments
                ?.getParcelableArray("movieList")
                ?.mapNotNull { it as? MovieUiModel }
                ?: emptyList()
        movieAdapter.updateMovies(movies)

        binding.buttonToggleView.setOnClickListener {
            currentViewType =
                when (currentViewType) {
                    MovieViewType.LIST -> MovieViewType.GRID
                    MovieViewType.GRID -> MovieViewType.LIST
                    else -> MovieViewType.LIST
                }
            updateLayoutMode()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFullMovieList.apply {
            layoutManager =
                when (currentViewType) {
                    MovieViewType.GRID -> GridLayoutManager(requireContext(), 3)
                    else -> LinearLayoutManager(requireContext())
                }
            adapter = movieAdapter
            setHasFixedSize(true)
        }
        updateToggleIcon()
    }

    private fun updateLayoutMode() {
        movieAdapter.setViewType(currentViewType)
        setupRecyclerView()
    }

    private fun updateToggleIcon() {
        val iconRes =
            when (currentViewType) {
                MovieViewType.LIST -> R.drawable.ic_grid
                MovieViewType.GRID -> R.drawable.ic_list
                else -> R.drawable.ic_grid
            }
        binding.buttonToggleView.setImageResource(iconRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
