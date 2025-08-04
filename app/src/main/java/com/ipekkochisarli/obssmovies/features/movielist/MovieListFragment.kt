package com.ipekkochisarli.obssmovies.features.movielist

import android.os.Bundle
import android.os.Parcelable
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
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentMovieListBinding
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import com.ipekkochisarli.obssmovies.features.search.presentation.SearchViewModel
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_LIST_DATA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieListFragmentData(
    val header: String,
    val movieList: List<MovieUiModel>,
) : Parcelable

@AndroidEntryPoint
class MovieListFragment : BaseFragment<FragmentMovieListBinding>(FragmentMovieListBinding::inflate) {
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var movieAdapter: MovieListAdapter

    private var data: MovieListFragmentData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable(MOVIE_LIST_DATA)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter =
            MovieListAdapter(
                viewType = MovieViewType.LIST,
            )

        binding.tvHeader.text = data?.header.orEmpty()

        setupRecyclerView()
        movieAdapter.submitList(data?.movieList.orEmpty())

        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                when (uiState.viewType) {
                    MovieViewType.LIST -> {
                        binding.recyclerViewFullMovieList.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.buttonToggleView.setImageResource(R.drawable.ic_grid)
                    }

                    MovieViewType.GRID -> {
                        binding.recyclerViewFullMovieList.layoutManager =
                            GridLayoutManager(requireContext(), 3)
                        binding.buttonToggleView.setImageResource(R.drawable.ic_list)
                    }

                    MovieViewType.POSTER -> {}
                }
                movieAdapter.setViewType(uiState.viewType)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFullMovieList.apply {
            adapter = movieAdapter
            setHasFixedSize(true)
        }
        setupToggleButton()
    }

    private fun setupToggleButton() {
        binding.buttonToggleView.setOnClickListener {
            viewModel.toggleViewType()
        }
    }
}
