package com.example.filmflash

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/movie/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MovieListAPIService {
    @GET("now_playing")
    fun getMovieListFromAPI(
        @Query("api_key") apiKey: String,
        @Query("page") pageNumber: Int
    ): Call<MovieListResponse>
}


data class MovieListResponse(
    @Json(name = "dates") val dates: Dates,
    @Json(name = "page") val page: Int,
    @Json(name = "results") val movies: List<Movie>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)

data class Dates(
    @Json(name = "maximum") val maximum: String,
    @Json(name = "minimum") val minimum: String
)


object MovieListAPI {
    val retrofitService: MovieListAPIService by lazy {
        retrofit.create(MovieListAPIService::class.java)
    }
}