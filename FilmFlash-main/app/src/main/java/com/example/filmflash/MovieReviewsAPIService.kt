package com.example.filmflash

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Path

private const val BASE_URL = "https://api.themoviedb.org/3/movie/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MovieReviewsAPIService {
    @GET("{movie_id}/reviews?api_key=${BuildConfig.API_KEY}")
    fun getMovieReviewsFromAPI(@Path("movie_id") movieID: Int):
            Call<MovieReview>
}

object MovieReviewsAPI {
    val retrofitService: MovieReviewsAPIService by lazy {
        retrofit.create(MovieReviewsAPIService::class.java)
    }
}

data class MovieReview(
    val id: Int,
    val page: Int,
    val results: List<Review>
)