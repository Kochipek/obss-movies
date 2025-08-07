package com.ipekkochisarli.obssmovies.features.contentdetail.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentContentDetailBinding
import com.ipekkochisarli.obssmovies.databinding.FragmentLoginBinding
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

        val movieId = arguments?.getInt(MOVIE_ID) ?: 0

        // !todo implement click logic
        adapter.onActionClicked = { detail, menuItemId ->
            when (menuItemId) {
                R.id.action_add_to_watch_later -> {}

                R.id.action_add_to_watched -> {}
            }
        }

        observeUiState()

        if (movieId != 0) {
            viewModel.loadMovieDetail(movieId)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
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
