package com.ipekkochisarli.obssmovies.features.contentdetail.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentContentDetailBinding
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.ContentDetailAdapter
import com.ipekkochisarli.obssmovies.features.contentdetail.presentation.adapter.ContentDetailItem
import com.ipekkochisarli.obssmovies.util.Constants.MOVIE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContentDetailFragment : BaseFragment<FragmentContentDetailBinding>(FragmentContentDetailBinding::inflate) {
    private val viewModel: ContentDetailViewModel by viewModels()
    private val adapter by lazy { ContentDetailAdapter() }

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
        adapter.onActionClicked = { detail, menuItemId ->
            when (menuItemId) {
                R.id.action_add_to_watch_later -> {
                    // TODO: Implement "add to watch later"
                }

                R.id.action_add_to_watched -> {
                    // TODO: Implement "add to watched"
                }
            }
        }

        adapter.onShareClicked = { text ->
            shareText(text)
        }
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

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                val items = mutableListOf<ContentDetailItem>()

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

                adapter.submitList(items)
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            }
        }
    }
}
