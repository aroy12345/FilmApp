package com.example.filmflash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.filmflash.databinding.FragmentReviewDetailsBinding
import io.noties.markwon.Markwon

class ReviewDetailsFragment : Fragment() {
    private var _binding: FragmentReviewDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ReviewDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title =
            (activity as AppCompatActivity).getString(R.string.review_details)
        _binding = FragmentReviewDetailsBinding.inflate(inflater, container, false)
        val content = args.content  // The raw content of the review
        val author = args.author  // The name of the author of the review
        val rating = args.rating  // The rating given by the review, a float number on scale of 10
        val avatarPath = args.avatarPath  // The image URL to the avatar of the author
        // Render the markdown content
        val markwon = Markwon.create(requireContext())
        val markdownContent = content.replace("\\r\\n", "\n")
        val spannedContent = markwon.toMarkdown(markdownContent)

        binding.apply {
            reviewItemContent.text = spannedContent
            reviewItemUsername.text = author
            if (avatarPath != null) Glide.with(requireContext()).load(avatarPath)
                .into(reviewItemProfilePicImage)
            if (rating < 0) {  // rating was null
                reviewItemRatingBar.visibility = View.GONE
            } else {
                reviewItemRatingBar.rating = rating / 2f
            }
        }
        return binding.root
    }
}