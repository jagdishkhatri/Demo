package com.example.myapplication.data.remote

import com.example.myapplication.model.APIResponse
import com.example.myapplication.model.PostModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    @GET("media-coverages?limit=100")
    fun getTopImages(): Call<APIResponse>

    @POST("v1/post/{user_id}")
    fun postData(@Path("user_id") user_id: String,@Body model: PostModel): Call<APIResponse>
}