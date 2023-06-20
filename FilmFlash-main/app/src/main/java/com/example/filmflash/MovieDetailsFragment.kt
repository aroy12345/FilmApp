package com.example.filmflash

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmflash.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private var movieId = 0

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var reviewsViewModel: MovieReviewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title =
            (activity as AppCompatActivity).getString(R.string.movie_details)
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        // Retrieve the movie id of this movie
        arguments?.let {
            movieId = it.getInt("movieId")
        }

        viewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        viewModel.getMovieInfo(movieId)

        reviewsViewModel = ViewModelProvider(this)[MovieReviewsViewModel::class.java]
        reviewsViewModel.getReviewsList(requireContext(), movieId)

        viewModel.movieInfo.observe(viewLifecycleOwner) {
            binding.apply {
                // The movie backdrop
                if (it.backdrop_path == null) {  // If there's no backdrop, use the logo image
                    movieBackdrop.setImageResource(R.drawable.logo_horizontal)
                } else {
                    val backdropPath =
                        "https://image.tmdb.org/t/p/original/" + it.backdrop_path.toString()
                    Glide.with(requireContext()).load(backdropPath).into(movieBackdrop)
                }
                // The movie title
                movieTitle.text = it.title
                // The movie tagline
                if (it.tagline.isNullOrBlank()) {
                    movieTagline.visibility = View.GONE
                    val layoutParams = movieTitle.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.margin_20dp)
                    movieTitle.layoutParams = layoutParams
                } else {
                    movieTagline.text = it.tagline
                }
                // The movie rating bar
                movieRatingBar.rating = (it.vote_average / 2).toFloat()
                // The movie rating score
                movieRating.text = getString(R.string.rating_template, it.vote_average)
                // The number of votes for the movie
                movieVotes.text = getString(R.string.votes_template, it.vote_count)
                // The movie length
                movieLength.text =
                    getString(R.string.length_template, it.runtime / 60, it.runtime % 60)
                // Whether the movie is adult-only
                movieAdult.text = if (it.adult) "Yes" else "No"
                // The movie release date
                movieReleaseDate.text = it.release_date
                // The movie genres
                var genreText = ""
                for ((index, genre) in it.genres.withIndex()) {
                    genreText += genre.name
                    if (index != it.genres.size - 1) {
                        genreText += ", "
                    }
                }
                movieGenres.text = genreText
                // The movie overview
                movieOverview.text = it.overview
                // The movie languages
                var languageText = ""
                for ((index, language) in it.spoken_languages.withIndex()) {
                    languageText += language.english_name
                    if (index != it.spoken_languages.size - 1) {
                        languageText += ", "
                    }
                }
                movieLanguages.text = languageText
                // Add a 15dp space between each review in the review section
                val itemSpacingDecoration =
                    ItemSpacingDecoration(resources.getDimensionPixelSize(R.dimen.margin_15dp))
                reviewListRv.addItemDecoration(itemSpacingDecoration)
            }
        }

        reviewsViewModel.reviewList.observe(viewLifecycleOwner, Observer {
            binding.apply {
                // The review section title
                reviewsTitle.text = getString(R.string.reviews_section_title, it.size)
                val reviewRV = reviewListRv
                // Toggle review section visibility
                if (it.size > 0) {
                    noReviewsText.visibility = View.GONE
                    reviewRV.visibility = View.VISIBLE
                } else {
                    noReviewsText.visibility = View.VISIBLE
                    reviewRV.visibility = View.GONE
                }
                it?.let {
                    val adapter = MovieReviewsAdapter(it)
                    reviewRV.adapter = adapter
                    adapter.setOnItemClickListener(object :
                        MovieReviewsAdapter.OnItemClickListener {
                        override fun onItemClick(itemView: View?, position: Int) {
                            val content = reviewsViewModel.reviewList.value!![position].content
                            val author = reviewsViewModel.reviewList.value!![position].author
                            val rating =
                                reviewsViewModel.reviewList.value!![position].authorDetails.rating
                            val ratingNum = rating?.toFloat() ?: -1f  // If no rating, set to -1
                            val avatarPath =
                                reviewsViewModel.reviewList.value!![position].authorDetails.avatarPath
                            val action =
                                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToReviewDetailsFragment(
                                    content,
                                    author,
                                    ratingNum,
                                    avatarPath
                                )
                            findNavController().navigate(action)
                        }
                    })
                }
            }
        })

        // Share button
        binding.shareButton.setOnClickListener {
            shareMovie()
        }
        return binding.root
    }

    private fun shareMovie() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, getString(
                R.string.share_template,
                binding.movieTitle.text,
                binding.movieReleaseDate.text
            )
        )
        try {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_prompt)))
        } catch (e: Exception) {
            Toast.makeText(context, getString(R.string.share_error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// A custom decoration consisted of white space of custom size between items
class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = spacing
    }
}
