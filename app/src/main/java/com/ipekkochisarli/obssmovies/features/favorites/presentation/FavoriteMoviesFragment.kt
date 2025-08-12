package com.ipekkochisarli.obssmovies.features.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.databinding.FragmentFavoriteMoviesBinding
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteListType
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment(R.layout.fragment_favorite_movies) {
    private val viewModel: FavoriteMoviesViewModel by viewModels()
    private lateinit var binding: FragmentFavoriteMoviesBinding

    private val adapter by lazy {
        MovieListAdapter(
            viewType = MovieViewType.LIST,
            onMovieClick = { movie ->
                val bundle = Bundle().apply { putInt(MOVIE_ID, movie.id) }
                findNavController().navigate(R.id.contentDetailFragment, bundle)
            },
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteMoviesBinding.bind(view)

        setupListeners()
        collectUiState()
        setupRecyclerView()
        viewModel.loadFavorites(FavoriteListType.WATCHED)
    }

    fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { movies ->
                adapter.submitList(movies)
            }
        }
    }

    fun setupRecyclerView() {
        binding.rvFavoriteMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteMovies.adapter = adapter
    }

    fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
