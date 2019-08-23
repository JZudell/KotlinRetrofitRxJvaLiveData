package com.expanse.computeraccount.mindbodyapp.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.expanse.computeraccount.mindbodyapp.R
import com.google.gson.JsonArray


//MainActivity Initiates a ViewModel to gather API info.
//MainActivity handles 2 different fragments depending on which API info the user wants to see
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    val fragmentManager = supportFragmentManager
    var fragmentCountries = Fragment()
    var fragmentProvinces = ProvinceFragment()
    lateinit var model: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun startUpMainActivity(){

        model = ViewModelProviders.of(this@MainActivity)[MainViewModel::class.java]


        // current keeps track of what fragment is visible,
        // after orientation change, this IF, ELSE block determines which fragment to show to the user.
        // the difference between the IF and the ELSE, is order of operation is swapped. which ever observer triggers last wins in this case
        val current = model.fragmentIdCurrent
        if(current==1){
            model.getProvinces().observe(this@MainActivity, Observer<JsonArray>{ provinceInfo ->
                showDetailFragment(provinceInfo)
            })
            model.getCountries().observe(this@MainActivity, Observer<JsonArray>{ countryInfo ->
                showMainFragment(countryInfo)
            })
        }else if(current==2){
            model.getCountries().observe(this@MainActivity, Observer<JsonArray>{ countryInfo ->
                showMainFragment(countryInfo)
            })
            model.getProvinces().observe(this@MainActivity, Observer<JsonArray>{ provinceInfo ->
                showDetailFragment(provinceInfo)
            })
        }
    }

    fun callProvinceApiOnViewModel(id:Int){

        println("STARTING PROVINCE MODEL")

        model.makeProvinceApiCall(id)

    }
    override fun onBackPressed() {
        if (fragmentProvinces.isResumed) {
            model.fragmentIdCurrent=1
            showMainFragment(model.getCountries().value!!)
        } else {
            this.finish()
        }
    }

    fun showDetailFragment(jsonArray: JsonArray){
        println("STARTING PROVINCE FRAGMENT")
        var bundle = Bundle()
        bundle.putString("jsonObjectAsStringProvince",jsonArray.toString())
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentProvinces.setArguments(bundle)
        fragmentTransaction.replace(com.expanse.computeraccount.mindbodyapp.R.id.fragment_containter, fragmentProvinces)
        fragmentTransaction.commit()
    }
    fun showMainFragment(jsonArray: JsonArray){
        var bundle = Bundle()
        bundle.putString("jsonObjectAsString",jsonArray.toString())
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentCountries = CountriesFragment()
        fragmentCountries.setArguments(bundle)
        fragmentTransaction.replace(com.expanse.computeraccount.mindbodyapp.R.id.fragment_containter, fragmentCountries)
        fragmentTransaction.commit()

    }
    override fun onStart(){
        super.onStart()
        Log.d(TAG,"onStart")

    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")
        startUpMainActivity()

    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }
}
