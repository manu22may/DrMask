package com.capgemini.drmask.retrofitdatabase

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface CovidDbInterface {
    @GET
    fun getCasesDetails(@Url url:String): Call<CovidDetails>

    companion object{

        val BASE_URL="https://api.rootnet.in/covid19-in/"

        fun getInstance(): CovidDbInterface {
            val builder = Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)
            val retrofit =builder.build()
            return retrofit.create(CovidDbInterface::class.java)

        }

    }

}