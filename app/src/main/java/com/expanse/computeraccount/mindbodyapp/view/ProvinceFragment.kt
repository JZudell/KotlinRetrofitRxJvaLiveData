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
import com.expanse.computeraccount.mindbodyapp.model.Province
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_province.*

//ProvinceFragment uses a RecyclerView to show user data
// Clicking on a RecyclerView item will open implicit intent for browser to search google for the name of the province and country.
// project directions mentions making touch feedback on lists. both color change and device vibration were implemented.
class ProvinceFragment : Fragment() {

    lateinit var mContext : Context
    lateinit var adapter:ProvinceRecyclerAdapter

    val TAG = "ProvinceFragment"
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
    var listOfProvince : ArrayList<Province> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        Log.d(TAG,"onCreateView")

        return inflater.inflate(com.expanse.computeraccount.mindbodyapp.R.layout.fragment_province, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")

        setUpListOfData()
        setupRecyclerView()
        println("LISTOFPROVINCE+++++++++++"+listOfProvince)
        displayData(listOfProvince)
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        (activity as MainActivity).hideLoadingDialog()

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
    fun setUpListOfData(){
        listOfProvince.clear()
        var bundle = arguments
        val jsonString = bundle?.get("jsonObjectAsStringProvince").toString()
        val jsonArray = JsonParser().parse(jsonString).asJsonArray
        for(i in 0..(jsonArray.size()-1)){
            val item = jsonArray.get(i).asJsonObject
            listOfProvince.add(Province(item.get("ID").toString(),item.get("CountryCode").toString(),item.get("Code").toString(),item.get("Name").toString()))
        }
    }
    fun displayData(listOfProvince: ArrayList<Province>){
        Log.d(TAG,"creating adapter")
        adapter = ProvinceRecyclerAdapter(listOfProvince)
        provinceRecyclerView.adapter = adapter
    }

    fun setupRecyclerView(){

        provinceRecyclerView.setHasFixedSize(true)
        provinceRecyclerView.layoutManager = LinearLayoutManager(mContext) as RecyclerView.LayoutManager?

    }



}