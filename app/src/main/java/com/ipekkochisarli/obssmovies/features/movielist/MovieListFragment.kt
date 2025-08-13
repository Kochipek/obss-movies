package com.ipekkochisarli.obssmovies.features.movielist

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.CustomLoadingDialog
import com.ipekkochisarli.obssmovies.common.ErrorDialog
import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentMovieListBinding
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_LIST_DATA
import com.ipekkochisarli.obssmovies.util.extensions.updateLayoutManagerWithState
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
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieAdapter: MovieListAdapter
    private var data: MovieListFragmentData? = null

    private val loadingDialog: CustomLoadingDialog by lazy {
        CustomLoadingDialog(requireContext())
    }

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
        viewModel.setMovieList(data?.movieList.orEmpty())

        movieAdapter =
            MovieListAdapter(
                viewType = MovieViewType.LIST,
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

        binding.tvHeader.text = data?.header.orEmpty()
        setupRecyclerView()

        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                loadingDialog.showLoading(uiState.isLoading)
                binding.recyclerViewFullMovieList.updateLayoutManagerWithState(
                    uiState.viewType,
                    binding.buttonToggleView,
                )
                movieAdapter.setViewType(uiState.viewType)
                movieAdapter.submitList(uiState.results)
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

    private fun navigateToContentDetail(movieId: Int) {
        findNavController().navigate(
            R.id.action_movieListFragment_to_contentDetailFragment,
            Bundle().apply { putInt(MOVIE_ID, movieId) },
        )
    }
}
