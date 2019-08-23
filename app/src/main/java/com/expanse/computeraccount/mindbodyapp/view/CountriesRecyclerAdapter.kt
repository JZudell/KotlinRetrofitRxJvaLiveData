package com.expanse.computeraccount.mindbodyapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.expanse.computeraccount.mindbodyapp.R
import com.expanse.computeraccount.mindbodyapp.model.Country
import kotlinx.android.synthetic.main.recycler_item_countries.view.*

class CountriesRecyclerAdapter(var storesList: ArrayList<Country>) : RecyclerView.Adapter<CountriesRecyclerAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_countries, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CountriesRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(storesList.get(position))
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return storesList.size
    }
    public fun getItem(position: Int) : Country {
        return storesList.get(position)
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