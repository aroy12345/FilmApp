package com.example.filmflash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.filmflash.databinding.FragmentMovieListBinding

class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MovieListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title =
            (activity as AppCompatActivity).getString(R.string.app_name)
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        // Get the view model holding the list of movies and observe it
        viewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
        viewModel.movieList.observe(viewLifecycleOwner) { movieList ->
            val recyclerView = binding.movieOverviewRv
            movieList?.let {
                val adapter = MovieListAdapter(it)
                recyclerView.adapter = adapter
                // If a movie is clicked, navigate to the details fragment for that movie
                adapter.setOnItemClickListener(object : MovieListAdapter.OnItemClickListener {
                    override fun onItemClick(itemView: View?, position: Int) {
                        val movieID = viewModel.movieList.value!![position].id
                        val action =
                            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
                                movieID
                            )
                        findNavController().navigate(action)
                    }
                })
            }
        }

        // Add a divider between each item in the list
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        binding.movieOverviewRv.addItemDecoration(itemDecoration)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}