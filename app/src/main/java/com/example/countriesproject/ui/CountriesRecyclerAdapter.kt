package com.example.countriesproject.ui

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesproject.R
import com.example.countriesproject.models.CountryObject
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou


class CountriesRecyclerAdapter(
    val context: Context,
    var data: ArrayList<CountryObject>,
    val listener: OnCountryClickListener
) : RecyclerView.Adapter<CountriesRecyclerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_recycler_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    fun refreshList(newData: ArrayList<CountryObject>) {
        data = newData
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivFlagImage: ImageView = itemView.findViewById(R.id.flag_imageView)
        private val tvCountryName: TextView = itemView.findViewById(R.id.name_text)
        private val tvNativeName: TextView = itemView.findViewById(R.id.native_name_txt)
        private val tvArea: TextView = itemView.findViewById(R.id.area_text)
        private val tvPopulation: TextView = itemView.findViewById(R.id.population_text)
        private val mainLayout: ConstraintLayout = itemView.findViewById(R.id.main_layout)

        fun onBind(item: CountryObject) {

            tvCountryName.text = item.name
            tvNativeName.text = item.nativeName
            tvArea.text = item.area?.toString()
            tvPopulation.text = item.population?.toString()

            val uri = Uri.parse(item.flagPhotoUrl)

            GlideToVectorYou.init().with(context).load(uri, ivFlagImage )


            mainLayout.setOnClickListener {
                listener.onCountryClick(item)
            }

        }
    }

    interface OnCountryClickListener {
        fun onCountryClick(item: CountryObject)
    }

}