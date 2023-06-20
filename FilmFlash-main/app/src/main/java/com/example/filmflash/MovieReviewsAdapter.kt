package com.example.filmflash

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmflash.databinding.ReviewItemBinding

class MovieReviewsAdapter(private val reviewList: MutableList<ReviewWithAll>) :
    RecyclerView.Adapter<MovieReviewsAdapter.ReviewItemHolder>() {

    private var _binding: ReviewItemBinding? = null
    private val binding get() = _binding!!

    inner class ReviewItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                // Triggers click upwards to the adapter on click
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MovieReviewsAdapter.ReviewItemHolder, position: Int) {
        val review = reviewList[position]

        binding.apply {
            val avPath = review.authorDetails.avatarPath
            if (avPath != null) {
                if (avPath.indexOf("https") != -1) {
                    val avatarPath = avPath.substring(1)
                    Glide.with(holder.itemView.context).load(avatarPath)
                        .into(reviewItemProfilePicImage)
                    review.authorDetails.avatarPath = avatarPath
                } else {
                    val avatarPath =
                        "https://image.tmdb.org/t/p/original/" + review.authorDetails.avatarPath
                    Glide.with(holder.itemView.context).load(avatarPath)
                        .into(reviewItemProfilePicImage)
                    review.authorDetails.avatarPath = avatarPath
                }
            }

            reviewItemUsername.text = review.author
            reviewItemContent.text = review.contentPreview
            if (review.authorDetails.rating == null) {
                reviewItemRatingBar.visibility = View.GONE
            } else {
                reviewItemRatingBar.visibility = View.VISIBLE
                reviewItemRatingBar.rating = (review.authorDetails.rating / 2).toFloat()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieReviewsAdapter.ReviewItemHolder {
        _binding = ReviewItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return ReviewItemHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return reviewList.size
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