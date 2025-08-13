package com.ipekkochisarli.obssmovies.features.movielist

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.CustomLoadingDialog
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentMovieListBinding
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import com.ipekkochisarli.obssmovies.util.extensions.updateLayoutManagerWithState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieListFragmentData(
    val header: String,
    val viewType: MovieViewType = MovieViewType.LIST,
) : Parcelable

@AndroidEntryPoint
class MovieListFragment : BaseFragment<FragmentMovieListBinding>(FragmentMovieListBinding::inflate) {
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieAdapter: MoviePagingAdapter
    private var sectionType: HomeSectionType? = null

    private val loadingDialog: CustomLoadingDialog by lazy { CustomLoadingDialog(requireContext()) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            sectionType = it.getSerializable("sectionType") as? HomeSectionType
        }

        setupAdapter()
        setupRecyclerView()
        setupNavigation()
        observeUiState()
        observeMovies()
    }

    private fun setupAdapter() {
        movieAdapter =
            MoviePagingAdapter(
                viewType = viewModel.uiState.value.viewType,
                onMovieClick = { navigateToContentDetail(it.id) },
            )
        binding.recyclerViewFullMovieList.adapter = movieAdapter
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFullMovieList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        binding.buttonToggleView.setOnClickListener {
            viewModel.toggleViewType()
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                loadingDialog.showLoading(state.isLoading)
                updateViewType(state.viewType)
            }
        }
    }

    private fun observeMovies() {
        sectionType?.let { type ->
            lifecycleScope.launch {
                viewModel.getMovies(type).collectLatest { pagingData ->
                    movieAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun updateViewType(viewType: MovieViewType) {
        binding.recyclerViewFullMovieList.updateLayoutManagerWithState(
            viewType,
            binding.buttonToggleView,
        )
        movieAdapter.setViewType(viewType)
    }

    private fun setupNavigation() {
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun navigateToContentDetail(movieId: Int) {
        findNavController().navigate(
            R.id.action_movieListFragment_to_contentDetailFragment,
            Bundle().apply { putInt(MOVIE_ID, movieId) },
        )
    }
}
