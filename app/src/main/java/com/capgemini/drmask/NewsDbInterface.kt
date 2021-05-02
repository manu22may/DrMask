package com.capgemini.drmask

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsDbInterface {
    @GET
    fun getHealthNews(@Url url:String): Call<ArticlesList>

    companion object{

        val BASE_URL="https://newsapi.org/"

        fun getInstance():NewsDbInterface{
            val builder = Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)
            val retrofit =builder.build()
            return retrofit.create(NewsDbInterface::class.java)

        }

    }

}