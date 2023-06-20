package com.example.filmflash

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmflash.databinding.MovieItemBinding

class MovieListAdapter(private val movieList: MutableList<Movie>) :
    RecyclerView.Adapter<MovieListAdapter.MovieItemHolder>() {

    inner class MovieItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = MovieItemBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                // Triggers click upwards to the adapter on click
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }
        }

        fun bind(movie: Movie) {
            val poster = binding.movieItemPoster
            val title = binding.movieItemTitle
            val ratingBar = binding.movieItemRatingBar
            val releaseDate = binding.movieItemReleaseDate
            val overview = binding.movieItemOverview

            val posterPath = "https://image.tmdb.org/t/p/original/" + movie.posterPath.toString()
            Glide.with(itemView.context).load(posterPath).into(poster)

            title.text = movie.title

            ratingBar.rating = (movie.voteAverage / 2).toFloat()

            releaseDate.text = "Released on: " + movie.releaseDate.toString()

            overview.text = movie.overview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieItemHolder(view)
    }

    override fun onBindViewHolder(holder: MovieItemHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    // Define the listener interface
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    // Define listener member variable
    private lateinit var listener: OnItemClickListener

    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}