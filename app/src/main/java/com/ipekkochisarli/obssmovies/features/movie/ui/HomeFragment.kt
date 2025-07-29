package com.ipekkochisarli.obssmovies.features.movie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.databinding.FragmentHomeBinding
import com.ipekkochisarli.obssmovies.features.movie.ui.adapter.CategorySectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //todo move to base fragment later
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private var categoryAdapter: CategorySectionAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewSections.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.uiStates.collect { states ->
                    categoryAdapter = CategorySectionAdapter(states)
                    binding.recyclerViewSections.adapter = categoryAdapter
                }
            }
        viewModel.loadMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}