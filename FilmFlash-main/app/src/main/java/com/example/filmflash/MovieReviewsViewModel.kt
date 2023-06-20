package com.example.filmflash

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import io.noties.markwon.Markwon


class MovieReviewsViewModel : ViewModel() {
    private val _reviewsList = MutableLiveData<MutableList<ReviewWithAll>>()
    val reviewList: LiveData<MutableList<ReviewWithAll>>
        get() = _reviewsList

    init {

    }

    fun getReviewsList(context: Context, movieID: Int) {
        MovieReviewsAPI.retrofitService.getMovieReviewsFromAPI(movieID).enqueue(object :
            Callback<MovieReview> {
            override fun onResponse(
                call: Call<MovieReview>,
                response: Response<MovieReview>
            ) {
                Log.i("SUI", response.body().toString())
                val reviews = response.body()?.results
                val markwon = Markwon.builder(context).build()
                val reviewsWithAll = mutableListOf<ReviewWithAll>()
                reviews?.forEach { review: Review ->

                    val reviewContent = review.content
                    val markdownContent = reviewContent.replace("\\r\\n", "\n")
                    val spannedContent = markwon.toMarkdown(markdownContent)


                    val contentReplacedNewLines = reviewContent.replace("\\r\\n", " ")
                    val contentRemovedMarkdown = contentReplacedNewLines.replace(
                        "[*_|~#`\\]\\[()^>]".toRegex(), ""
                    )

                    val reviewWithAll = ReviewWithAll(
                        review.author,
                        review.authorDetails, review.content,
                        spannedContent, contentRemovedMarkdown
                    )
                    reviewsWithAll.add(reviewWithAll)
                }
                _reviewsList.value = reviewsWithAll.toMutableList()
            }

            override fun onFailure(call: Call<MovieReview>, t: Throwable) {
                Log.i("API", "ERROR: " + t.message)
            }

        })
    }
}