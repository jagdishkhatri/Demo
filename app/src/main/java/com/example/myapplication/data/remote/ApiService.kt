package com.example.myapplication.data.remote

import com.example.myapplication.model.APIResponse
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {

    @GET("media-coverages?limit=100")
    fun getTopImages(): Call<APIResponse>
}