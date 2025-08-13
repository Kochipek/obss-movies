package com.ipekkochisarli.obssmovies.features.contentdetail.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.CustomLoadingDialog
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentContentDetailBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.ContentDetailAdapter
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.ContentDetailItem
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContentDetailFragment : BaseFragment<FragmentContentDetailBinding>(FragmentContentDetailBinding::inflate) {
    private val viewModel: ContentDetailViewModel by viewModels()
    private val adapter by lazy { ContentDetailAdapter() }

    private val loadingDialog: CustomLoadingDialog by lazy {
        CustomLoadingDialog(requireContext())
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()

        val movieId = arguments?.getInt(MOVIE_ID) ?: 0
        if (movieId != 0) {
            viewModel.loadMovieDetail(movieId)
        }

        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        adapter.onShareClicked = { text ->
            shareText(text)
        }
        adapter.onVideoClicked = { videoUrl ->
            navigateToVideoUrl(videoUrl)
        }
        adapter.onSimilarMovieClick = { movie ->
            navigateToMovieDetail(movie.id)
        }
        adapter.onWatchLaterClicked = {
            viewModel.toggleFavoriteStatus(LibraryCategoryType.WATCH_LATER)
            viewModel.uiState.value.detail?.let { detail ->
                showListActionMessage(
                    detail.title,
                    R.string.watch_later_toast,
                    R.string.watch_later_removed_toast,
                    !viewModel.uiState.value.isAddedWatchLater,
                )
            }
        }

        adapter.onWatchedClicked = {
            viewModel.toggleFavoriteStatus(LibraryCategoryType.WATCHED)
            viewModel.uiState.value.detail?.let { detail ->
                showListActionMessage(
                    detail.title,
                    R.string.watched_toast,
                    R.string.watched_removed_toast,
                    !viewModel.uiState.value.isWatched,
                )
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                val items = mutableListOf<ContentDetailItem>()

                loadingDialog.showLoading(state.isLoading)

                state.detail?.let {
                    items.add(ContentDetailItem.Header(it))
                }

                if (state.credits.isNotEmpty()) {
                    items.add(ContentDetailItem.SectionCast(state.credits))
                }

                if (state.videos.isNotEmpty()) {
                    items.add(ContentDetailItem.SectionVideos(state.videos))
                }

                if (state.similar.isNotEmpty()) {
                    items.add(ContentDetailItem.SectionSimilarMovies(state.similar))
                }
                adapter.submitList(items, state.isAddedWatchLater, state.isWatched)
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun navigateToVideoUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply { data = url.toUri() }
        startActivity(intent)
    }

    private fun navigateToMovieDetail(movieId: Int) {
        val bundle = Bundle().apply { putInt(MOVIE_ID, movieId) }
        findNavController().navigate(R.id.contentDetailFragment, bundle)
    }

    private fun showListActionMessage(
        movieTitle: String,
        addResId: Int,
        removeResId: Int,
        added: Boolean,
    ) {
        val message =
            if (added) {
                getString(addResId, movieTitle)
            } else {
                getString(removeResId, movieTitle)
            }

        Snackbar
            .make(binding.root, message, Snackbar.LENGTH_LONG)
            .setTextColor(requireContext().getColor(android.R.color.white))
            .setActionTextColor(requireContext().getColor(android.R.color.holo_red_dark))
            .setAction(getString(R.string.go_to_library)) {
                if (isAdded) {
                    findNavController().navigate(R.id.action_contentDetailFragment_to_libraryFragment)
                }
            }.show()
    }

    private fun shareText(text: String) {
        val shareIntent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
        val chooser = Intent.createChooser(shareIntent, getString(R.string.share))
        startActivity(chooser)
    }
}
