package com.ipekkochisarli.obssmovies.features.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipekkochisarli.obssmovies.databinding.FragmentMovieListBinding
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import com.ipekkochisarli.obssmovies.features.home.ui.adapter.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieListAdapter(emptyList())
        binding.recyclerViewFullMovieList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter
            setHasFixedSize(true)
        }

        val movies = arguments?.getParcelableArray("movieList")?.map { it as MovieUiModel } ?: emptyList()
        movieAdapter.updateMovies(movies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
