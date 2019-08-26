package com.expanse.computeraccount.mindbodyapp.view

import android.content.Context
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.expanse.computeraccount.mindbodyapp.model.Country
import kotlinx.android.synthetic.main.recycler_item_countries.view.*



class CountriesRecyclerAdapter(var countriesList: ArrayList<Country>) : RecyclerView.Adapter<CountriesRecyclerAdapter.ViewHolder>() {

    lateinit var mContext:Context
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.expanse.computeraccount.mindbodyapp.R.layout.recycler_item_countries, parent, false)
        mContext = parent.context
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CountriesRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(countriesList.get(position))
        //R.drawable.button_press_background handles changing color of view when pressed states
        holder.itemView.setBackgroundResource(com.expanse.computeraccount.mindbodyapp.R.drawable.button_press_background)
        holder.itemView.setOnClickListener{

            (mContext as MainActivity).callProvinceApiFromViewModel(countriesList.get(position).id)
            //vibrates phone when view pressed
            val v = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(10) // 5000 miliseconds = 5 seconds
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return countriesList.size
    }
    public fun getItem(position: Int) : Country {
        return countriesList.get(position)
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(country: Country) {

            val baseImageUrl = "https://raw.githubusercontent.com/hjnilsson/country-flags/master/png250px/"
            val imageUrl = baseImageUrl+country.code.replace("\"","").toLowerCase()+".png"
            itemView.country_info.setText(country.name.replace("\"",""))

            //new Coil image library for Kotlin takes one line after added dependencies.
            //Coil automatically Caches as well.
            itemView.flag_imageview.load(imageUrl)

        }
    }

}