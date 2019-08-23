package com.expanse.computeraccount.mindbodyapp.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expanse.computeraccount.mindbodyapp.api.ApiService
import com.expanse.computeraccount.mindbodyapp.api.RetrofitClientCountries
import com.expanse.computeraccount.mindbodyapp.api.RetrofitClientProvinces
import com.google.gson.JsonArray
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


//MainViewModel class uses retrofit to execute API calls and the API responses are returned by RxJava as observables.
// MainViewModel class then emits LiveData objects to alert MainActivity of data update.
class MainViewModel : ViewModel() {

    val TAG = "MainViewModel"
    internal lateinit var jsonApi: ApiService
    internal lateinit var compositeDisposable: CompositeDisposable
    var fragmentIdCurrent: Int = 1 // keeps track of which fragment is visible to user


    //This is called when ViewModel.countriesInfo.observe() gets called in MainActivity
    //
    private val countriesInfo: MutableLiveData<JsonArray> by lazy {
        MutableLiveData<JsonArray>().also {
            Log.d(TAG,"ViewModel Countries Initiated")

            makeCountryApiCall()
        }
    }


    //this is called when ViewModel.provinceInfo.observe() gets called in MainActivity
    //
    private val provinceInfo: MutableLiveData<JsonArray> by lazy {
        MutableLiveData<JsonArray>().also {
            Log.d(TAG,"ViewModel Provinces Initiated")

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"onCleared")

    }

    fun getCountries(): LiveData<JsonArray>
    {
        return countriesInfo
    }
    fun getProvinces(): LiveData<JsonArray>
    {
        return provinceInfo
    }

    fun makeCountryApiCall()
    {
        fragmentIdCurrent=1
        Log.d(TAG,"Making Country Api Call*******************")
        val retrofit = RetrofitClientCountries.instance
        jsonApi = retrofit.create(ApiService::class.java)
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(jsonApi.countries
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{countries-> printJsonCountries(countries)}
        )
    }

    fun makeProvinceApiCall(id:Int){

        fragmentIdCurrent=2
        Log.d(TAG,"Making Province Api Call*******************")
        val retrofit = RetrofitClientProvinces.instance
        jsonApi = retrofit.create(ApiService::class.java)
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(jsonApi.getProvincesApi(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{provinces-> printJsonProvince(provinces)}
        )
    }
    fun printJsonCountries(jsonArray: JsonArray){
        countriesInfo.setValue(jsonArray)
    }
    fun printJsonProvince(jsonArray: JsonArray){
        provinceInfo.setValue(jsonArray)
    }
}