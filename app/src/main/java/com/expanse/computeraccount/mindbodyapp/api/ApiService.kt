package com.expanse.computeraccount.mindbodyapp.api

import com.google.gson.JsonArray
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

//Retrofit interface class holding API functions
interface ApiService{

    //notation when no parameter needed. the return type is a Observable, possible with RxJava, instead of extending a "DefaultResponse"
    @get:GET("country")
    val countries: Observable<JsonArray>

    //notation when parameter needed . the return type is a Observable, possible with RxJava, instead of extending a "DefaultResponse"
    @GET("{id}/province")
    fun getProvincesApi(@Path("id") id: Int): Observable<JsonArray>

}