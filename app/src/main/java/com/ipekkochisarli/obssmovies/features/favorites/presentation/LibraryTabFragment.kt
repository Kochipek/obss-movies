package com.ipekkochisarli.obssmovies.features.favorites.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.core.presentation.SwipeToDeleteCallback
import com.ipekkochisarli.obssmovies.databinding.FragmentSavedMoviesBinding
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import com.ipekkochisarli.obssmovies.util.Constants
import com.ipekkochisarli.obssmovies.util.Constants.ARG_LIST_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LibraryTabFragment : BaseFragment<FragmentSavedMoviesBinding>(FragmentSavedMoviesBinding::inflate) {
    private val viewModel: FavoriteMoviesViewModel by viewModels()
    private lateinit var listType: LibraryCategoryType

    private val adapter by lazy {
        MovieListAdapter(
            viewType = MovieViewType.LIST,
            onMovieClick = { movie ->
                val bundle = Bundle().apply { putInt(Constants.MOVIE_ID, movie.id) }
                findNavController().navigate(R.id.contentDetailFragment, bundle)
            },
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        listType = requireArguments().getSerializable(ARG_LIST_TYPE) as LibraryCategoryType

        setupRecyclerView()
        collectUiState()

        viewModel.loadFavorites(listType)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { movies ->
                adapter.submitList(movies)
                binding.tvEmptyState.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
                binding.rvFavoriteMovies.visibility =
                    if (movies.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvFavoriteMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteMovies.adapter = adapter

        val swipeCallback =
            SwipeToDeleteCallback { position ->
                val movie = adapter.currentList.getOrNull(position) ?: return@SwipeToDeleteCallback
                viewModel.removeFavoriteMovie(movie.id, listType)
            }

        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvFavoriteMovies)
    }

    companion object {
        fun newInstance(listType: LibraryCategoryType) =
            LibraryTabFragment().apply {
                arguments =
                    Bundle().apply {
                        putSerializable(ARG_LIST_TYPE, listType)
                    }
            }
    }
}
