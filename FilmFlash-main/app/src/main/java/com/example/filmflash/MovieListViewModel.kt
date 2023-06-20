package com.example.filmflash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewModel : ViewModel() {
    private val _movieList = MutableLiveData<MutableList<Movie>>()
    val movieList: LiveData<MutableList<Movie>>
        get() = _movieList

    init {
        getMovieList()
    }

    fun getMovieList() {
        MovieListAPI.retrofitService.getMovieListFromAPI(BuildConfig.API_KEY, 1)
            .enqueue(object :
                Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    _movieList.value = response.body()?.movies?.toMutableList()
                    Log.i("SUI", response.toString())
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    Log.i("API", "ERROR: " + t.message)
                }

            })
    }

}