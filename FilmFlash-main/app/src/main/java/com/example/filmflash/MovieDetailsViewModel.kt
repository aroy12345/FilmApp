package com.example.filmflash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel : ViewModel() {
    private val _movieInfo = MutableLiveData<MovieInfo>()
    val movieInfo: LiveData<MovieInfo>
        get() = _movieInfo

    fun getMovieInfo(movieID: Int) {
        MovieInfoAPI.retrofitService.getMovieInfoFromAPI(
            movieId = movieID,
            apiKey = BuildConfig.API_KEY
        ).enqueue(object : Callback<MovieInfo> {
            override fun onResponse(call: Call<MovieInfo>, response: Response<MovieInfo>) {
                _movieInfo.value = response.body()
                Log.i("SUI", response.toString())
            }

            override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                Log.i("API", "ERROR: " + t.message)
            }

        })
    }


}