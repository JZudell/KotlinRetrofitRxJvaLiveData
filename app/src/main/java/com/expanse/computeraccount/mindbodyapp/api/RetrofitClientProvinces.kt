package com.expanse.computeraccount.mindbodyapp.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientProvinces {

    private const val BASE_URL = "https://connect.mindbodyonline.com/rest/worldregions/country/"

    private var ourInstance : Retrofit?=null

    val instance: Retrofit
        get() {
            //Singleton check
            if(ourInstance == null)
            {
                ourInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            }
            return ourInstance!!
        }
}