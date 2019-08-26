package com.expanse.computeraccount.mindbodyapp.view

import android.content.Context
import android.net.Uri
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.expanse.computeraccount.mindbodyapp.R
import com.expanse.computeraccount.mindbodyapp.model.Province
import kotlinx.android.synthetic.main.recycler_item_province.view.*
import java.net.URLEncoder

class ProvinceRecyclerAdapter(var provinceList: ArrayList<Province>) : RecyclerView.Adapter<ProvinceRecyclerAdapter.ViewHolder>() {

    lateinit var mContext:Context
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_province, parent, false)
        mContext = parent.context
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ProvinceRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(provinceList.get(position))
        //R.drawable.button_press_background handles view color change with button pressed states
        holder.itemView.setBackgroundResource(R.drawable.button_press_background)
        holder.itemView.setOnClickListener{

            // val item = adapter.getItem(position)
            val query = provinceList.get(position).name+" "+provinceList.get(position).countryCode
            //Strings coming from api Json data coming have double quotes as index's 0 and n-1
            //user facing UI needs these double quotes removed, but turns out leaving the double quotes in the query returns better results.
            //test case in particular is : Alaska, US     instead of: "Alaska", "US"
            //results: searching without double quotes results in a credit union being the top search result, instead of the state with accompanied map
            val escapedQuery = URLEncoder.encode(query, "UTF-8")
            val uri = Uri.parse("http://www.google.com/#q=$escapedQuery")
            (mContext as MainActivity).makeNewCall(uri)

            //this vibrates device on button click
            val v = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(10) // 5000 miliseconds = 5 seconds

        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return provinceList.size
    }
    public fun getItem(position: Int) : Province {
        return provinceList.get(position)
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(province: Province) {

            val dataString = province.name+", "+province.countryCode

            itemView.province_item.setText(dataString.replace("\"",""))

        }
    }

}