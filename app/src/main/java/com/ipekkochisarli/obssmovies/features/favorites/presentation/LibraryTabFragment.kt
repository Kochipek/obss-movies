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
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvFavoriteMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteMovies.adapter = adapter
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
