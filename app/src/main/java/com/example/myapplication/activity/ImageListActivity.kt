package com.example.myapplication.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.MarginItemDecoration
import com.example.myapplication.R
import com.example.myapplication.adapter.ImageAdpater
import com.example.myapplication.data.remote.ApiService
import com.example.myapplication.data.remote.instanceApi
import com.example.myapplication.databinding.ActivityImageListBinding
import com.example.myapplication.model.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageListActivity : AppCompatActivity() {

    private var response: Call<APIResponse>? = null
    private var apiService: ApiService? = null
    private lateinit var binding: ActivityImageListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mainRecycler.addItemDecoration(MarginItemDecoration(10))
        callAPI()

        binding.mainRecycler.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // check scroll & call API with next page & update adapter
        }
    }

    private fun callAPI() {
        apiService = instanceApi.client?.create(ApiService::class.java)

        response = apiService?.getTopImages()
        response?.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                binding.mainProgress.isVisible = false
                response.body()?.let {
                    val adapter = ImageAdpater(response.body()!!, this@ImageListActivity)
                    binding.mainRecycler.adapter = adapter
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                binding.mainProgress.isVisible = false
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        response?.cancel()
    }
}