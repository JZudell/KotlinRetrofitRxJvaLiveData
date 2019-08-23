package com.expanse.computeraccount.mindbodyapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.expanse.computeraccount.mindbodyapp.R
import com.expanse.computeraccount.mindbodyapp.model.Province
import kotlinx.android.synthetic.main.recycler_item_province.view.*

class ProvinceRecyclerAdapter(var storesList: ArrayList<Province>) : RecyclerView.Adapter<ProvinceRecyclerAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_province, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ProvinceRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(storesList.get(position))
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return storesList.size
    }
    public fun getItem(position: Int) : Province {
        return storesList.get(position)
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(province: Province) {

            val dataString = province.name+", "+province.countryCode
            itemView.province_item.setText(dataString)



//            itemView.textView.text = user.get("address").toString()
//
//            val photoUrl: String = user.get("storeLogoURL").toString().replace("\"","")
//
//            itemView.imageView.load(photoUrl)

        }
    }

}