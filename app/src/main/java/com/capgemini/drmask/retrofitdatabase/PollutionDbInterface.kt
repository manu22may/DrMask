package com.capgemini.drmask.retrofitdatabase

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface PollutionDbInterface {
    @GET
    fun getPollutionDetails(@Url url:String): Call<PollutionDetails>

    companion object{

        val BASE_URL="https://api.openweathermap.org/data/2.5/"

        fun getInstance(): PollutionDbInterface {
            val builder = Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)
            val retrofit =builder.build()
            return retrofit.create(PollutionDbInterface::class.java)

        }

    }

}