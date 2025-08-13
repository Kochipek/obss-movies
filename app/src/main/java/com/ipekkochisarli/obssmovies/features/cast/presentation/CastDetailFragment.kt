package com.ipekkochisarli.obssmovies.features.cast.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentCastDetailBinding
import com.ipekkochisarli.obssmovies.features.cast.domain.CastUiModel
import com.ipekkochisarli.obssmovies.util.extensions.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CastDetailFragment : BaseFragment<FragmentCastDetailBinding>(FragmentCastDetailBinding::inflate) {
    private val viewModel: CastViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val castId = arguments?.getInt("castId") ?: return

        observeUiState()

        viewModel.loadCastDetail(castId)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->

                state.error?.let { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }

                state.castDetail?.let { bindDetail(it) }
            }
        }
    }

    private fun bindDetail(detail: CastUiModel) {
        binding.profileImage.loadImage(detail.profileImageUrl)
        binding.nameText.text = detail.name
        binding.birthdayText.text = detail.birthday?.let { "Birthday: $it" }.orEmpty()
        binding.placeOfBirthText.text = detail.placeOfBirth?.let { "Place of Birth: $it" }.orEmpty()
        binding.departmentText.text = detail.knownForDepartment?.let { "Department: $it" }.orEmpty()
        binding.biographyText.text = detail.biography
    }
}
