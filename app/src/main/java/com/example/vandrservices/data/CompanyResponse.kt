package com.example.vandrservices.data

import com.google.gson.annotations.SerializedName

data class CompanyResponse(
    @SerializedName("pk") val id:Int,
    @SerializedName("name") val name:String,
    @SerializedName("imagentess")  val url:String?)

data class CompanyUI(
    val id: Int,
    val name: String,
    val imageUrl: String? = null,
    val imageRes: Int? = null
)