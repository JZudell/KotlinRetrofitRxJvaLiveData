package com.expanse.computeraccount.mindbodyapp.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.expanse.computeraccount.mindbodyapp.R
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

//MainActivity Initiates a ViewModel to gather API info.
//MainActivity handles 2 different fragments depending on which API info the user wants to see
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    val fragmentManager = supportFragmentManager
    var fragmentCountries = Fragment()
    var fragmentProvinces = ProvinceFragment()

    lateinit var model: MainViewModel

    lateinit var loadingDialog: AlertDialog
    lateinit var noProvinceDialog:AlertDialog
    lateinit var errorDialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.expanse.computeraccount.mindbodyapp.R.layout.activity_main)
        startUpMainActivity()

    }
    fun makeNewCall(uri: Uri){
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun startUpMainActivity(){

        model = ViewModelProviders.of(this@MainActivity)[MainViewModel::class.java]

        // val current keeps track of what fragment is visible,
        // after orientation change, this IF, ELSE block determines which fragment to show to the user.
        // the difference between the IF and the ELSE, is order of operation is swapped. which ever observer triggers last wins in this case
        //
        //  initially i had model.provinces().observe(...) within callProvinceApiFromViewModel(id:int), but the viewmodel would
        // emit old data when going back and forth between fragments
        val current = model.fragmentIdCurrent
        setLoadingDialog()
        println(current.toString()+"+++++++++++++++++++++++")

        //added current variable to keep track of which fragment user sees,
        //this first if() statement is a solution to re inflate the correct fragment after MainActivity is destroyed
        //such as device rotation.
        if(current==1){
            model.getProvinces().observe(this@MainActivity, Observer<JsonArray>{ provinceInfo ->
                if(provinceInfo.size()==0){
                    loadingDialog.dismiss()
                    showNoProvinceDialog()
                }else{
                    //added inner most if() statements with .isResumed as a solution to return to the correct fragment after
                    //activity re-started but from being paused
                    //such as returning to app by pressing back button after browser app was in focus
                    if (!fragmentProvinces.isResumed){
                        println("PROVINCEINFO 1"+provinceInfo)
                        showProvinceFragment(provinceInfo)
                    }
                }

            })
            model.getCountries().observe(this@MainActivity, Observer<JsonArray>{ countryInfo ->
                 if (!fragmentCountries.isResumed){
                     showCountryFragment(countryInfo)
                 }
            })
        }else if(current==2){
            model.getCountries().observe(this@MainActivity, Observer<JsonArray>{ countryInfo ->
                if (!fragmentCountries.isResumed){
                    showCountryFragment(countryInfo)
                }
            })
            model.getProvinces().observe(this@MainActivity, Observer<JsonArray>{ provinceInfo ->
                if(provinceInfo.size()==0){
                    loadingDialog.dismiss()
                    showNoProvinceDialog()
                }else{
                    if (!fragmentProvinces.isResumed){
                        println("PROVINCEINFO 2"+provinceInfo)
                        showProvinceFragment(provinceInfo)
                    }
                }

            })
        }

    }

    fun callProvinceApiFromViewModel(id:Int){

        setLoadingDialog()
        println("STARTING PROVINCE MODEL")

        model.makeProvinceApiCall(id)

    }
    override fun onBackPressed() {
        if (fragmentProvinces.isResumed) {
            model.fragmentIdCurrent=1
            showCountryFragment(model.getCountries().value!!)
        } else {
            this.finish()
        }
    }

    fun showProvinceFragment(jsonArray: JsonArray){
        val checkJsonForError = jsonArray.get(0).asJsonObject.toString()

        if((checkJsonForError.contains("\"Code\""))&&checkJsonForError.contains("\"CountryCode\"")){

            println("STARTING PROVINCE FRAGMENT")
            var bundle = Bundle()
            bundle.putString("jsonObjectAsStringProvince",jsonArray.toString())
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentProvinces.setArguments(bundle)
            fragmentTransaction.replace(com.expanse.computeraccount.mindbodyapp.R.id.fragment_containter, fragmentProvinces)
            fragmentTransaction.commit()

        }else{
            showErrorDialog(jsonArray.toString(),2)
        }
    }
    fun showCountryFragment(jsonArray: JsonArray){
        val checkJsonForError = jsonArray.get(0).asJsonObject.toString()
        if((checkJsonForError.contains("\"Code\""))&&checkJsonForError.contains("\"PhoneCode\"")){

            var bundle = Bundle()
            bundle.putString("jsonObjectAsString",jsonArray.toString())
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentCountries = CountriesFragment()
            fragmentCountries.setArguments(bundle)
            fragmentTransaction.replace(com.expanse.computeraccount.mindbodyapp.R.id.fragment_containter, fragmentCountries)
            fragmentTransaction.commit()

        }else{
            showErrorDialog(jsonArray.toString(),1)
        }

    }
    override fun onStart(){
        super.onStart()
        Log.d(TAG,"onStart")

    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")


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


    fun hideLoadingDialog(){
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
    }
    //Dialog setup laid out here, easily customizable to build visualy unique popups for the business app
    //Ideally this would be
    fun setLoadingDialog() {

        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = getString(R.string.loading)

        tvText.setTextColor(getColorWrapper(this, com.expanse.computeraccount.mindbodyapp.R.color.colorLoadingText))
        tvText.textSize = 20f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setView(ll)

        loadingDialog = builder.create()
        loadingDialog.show()
        val window = loadingDialog.getWindow()
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(window.getAttributes())
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            window.setAttributes(layoutParams)
        }
    }



    //Instead of adding the whole support V4 library for only accessing R.color, this version check will handle it
    //getResources().getColor(..) is deprecated since v23
    fun getColorWrapper(context: Context, id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getColor(id)
        } else {

            context.getResources().getColor(id)
        }
    }


    // showErrorDialog is called when retrofit returns data other than expected.
    // typeOfDialog = 1 or 2 determines which API is re-called. 1 for countries, 2 for provinces

    private fun showErrorDialog(errorString: String, typeOfDialog:Int){
        // Late initialize an alert loadingDialog object


        val jsonArray = JsonParser().parse(errorString) as JsonArray

        val jsonObject = jsonArray.get(1) as JsonObject
        val countryId = jsonObject.get("CountryCode").toString().replace("\"","")

        // Initialize a new instance of alert loadingDialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert loadingDialog
        builder.setTitle(getString(R.string.error_title))

        // Set a message for alert loadingDialog
        builder.setMessage(getString(R.string.error_message)+errorString)

        // On click listener for loadingDialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> if(typeOfDialog==1)startUpMainActivity()else callProvinceApiFromViewModel(countryId.toInt())
                DialogInterface.BUTTON_NEGATIVE -> dismissErrorDialog()
            }
        }

        // Set the alert loadingDialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)

        // Set the alert loadingDialog negative/no button
        builder.setNegativeButton("NO",dialogClickListener)

        // Initialize the AlertDialog using builder object
        errorDialog = builder.create()

        // Finally, display the alert loadingDialog
        errorDialog.show()
    }
    fun dismissErrorDialog(){
        if(errorDialog.isShowing){
            errorDialog.dismiss()
        }
        if (loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
    }


    private fun showNoProvinceDialog(){

        // Initialize a new instance of alert loadingDialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert loadingDialog
        builder.setTitle(getString(R.string.no_province_title))

        // Set a message for alert loadingDialog
        builder.setMessage(getString(R.string.no_province))

        // On click listener for loadingDialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> dismissProvinceDialog()

            }
        }

        // Set the alert loadingDialog positive/yes button
        builder.setPositiveButton(getString(R.string.ok),dialogClickListener)

        // Initialize the AlertDialog using builder object
        noProvinceDialog = builder.create()

        // Finally, display the alert loadingDialog
        noProvinceDialog.show()
    }
    fun dismissProvinceDialog(){
        if(noProvinceDialog.isShowing){
            noProvinceDialog.dismiss()
        }
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
    }


}
