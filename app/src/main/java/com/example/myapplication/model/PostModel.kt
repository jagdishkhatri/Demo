package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

class PostModel {
    @SerializedName("question")
    var question: String? = null

    @SerializedName("answer")
    var answer: String? = null

}