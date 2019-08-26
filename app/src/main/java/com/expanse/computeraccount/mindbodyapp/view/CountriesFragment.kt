package com.expanse.computeraccount.mindbodyapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.expanse.computeraccount.mindbodyapp.model.Country
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_countries.*


//CountriesFragment uses a RecyclerView to show data to user.
//showing flag images is handled by Coil image library for Kotlin
//// project directions mentions making touch feedback on lists. both color change and device vibration were implemented.
class CountriesFragment : Fragment() {

    lateinit var mContext : Context
    lateinit var adapter:CountriesRecyclerAdapter

    val TAG = "CountriesFragment"
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context!=null){
            mContext = context
        }
        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(com.expanse.computeraccount.mindbodyapp.R.layout.fragment_countries, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")
        var bundle = arguments
        val jsonString = bundle?.get("jsonObjectAsString").toString()
        setupRecyclerView()
        val jsonArray = JsonParser().parse(jsonString).asJsonArray
        displayData(jsonArray)

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        (activity as MainActivity).hideLoadingDialog()
    }
    fun displayData(jsonArray: JsonArray ){
        Log.d(TAG,"creating adapter")
        adapter = CountriesRecyclerAdapter(parseJson(jsonArray))
        countriesRecyclerView.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }


    private  fun parseJson(jsonArray: JsonArray): ArrayList<Country>{
        val myList : ArrayList<Country> = ArrayList()

       for ( i in 0..(jsonArray.size()-1)){
           val item = jsonArray.get(i).asJsonObject
           myList.add(Country(item.get("ID").asInt,item.get("Name").toString(),item.get("Code").toString(),item.get("PhoneCode").toString()))
       }

        return myList
    }


    fun setupRecyclerView(){

        countriesRecyclerView.setHasFixedSize(true)
        countriesRecyclerView.layoutManager = LinearLayoutManager(mContext) as RecyclerView.LayoutManager?

    }




}