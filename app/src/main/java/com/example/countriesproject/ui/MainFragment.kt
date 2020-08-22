package com.example.countriesproject.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesproject.R
import com.example.countriesproject.SortType
import com.example.countriesproject.models.CountryObject
import com.example.countriesproject.viewModel.MainViewModel


class MainFragment:Fragment(), CountriesRecyclerAdapter.OnCountryClickListener {
    private var countriesRecyclerData:ArrayList<CountryObject> = ArrayList()
    private lateinit var viewModel : MainViewModel
    private lateinit var recyclerDataAdapter:CountriesRecyclerAdapter
    private var currentCountry:String? = null
    private var isAreaDescending = false
    private var isNameDescending = false
    private var isPopulationDescending = false

    companion object{
        const val COUNTRIES_DATA = "countries_data"
        const val CURRENT_COUNTRY = "current_country"
        fun newInstance(data:ArrayList<CountryObject>):MainFragment{
            val fragment:MainFragment = MainFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(COUNTRIES_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
        fun newInstance(data:ArrayList<CountryObject>, currentCountry:String?):MainFragment{
            val fragment:MainFragment = MainFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(COUNTRIES_DATA, data)
            bundle.putString(CURRENT_COUNTRY, currentCountry)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.main_fragment_layout, container, false)
        val title:TextView = layout.findViewById(R.id.fragment_title)
        val recyclerView : RecyclerView = layout.findViewById(R.id.my_recyclerView)
        val filterByName:LinearLayout = layout.findViewById(R.id.filter_name_layout)
        val filterByArea:LinearLayout = layout.findViewById(R.id.filter_area_layout)
        val filterByPopulate:LinearLayout = layout.findViewById(R.id.filter_populate_layout)
        val nameArrow:ImageView = layout.findViewById(R.id.name_arrow)
        val areaArrow:ImageView = layout.findViewById(R.id.area_arrow)
        val populateArrow:ImageView = layout.findViewById(R.id.populate_arrow)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }

        arguments?.let {
            countriesRecyclerData = it.getParcelableArrayList<CountryObject>(COUNTRIES_DATA) as ArrayList<CountryObject>
            currentCountry = it.getString(CURRENT_COUNTRY)
        }

        if (!currentCountry.isNullOrEmpty()){
            title.text = String.format(getString(R.string.borders_title), currentCountry)
        }else{
            title.text = getString(R.string.all_countries_title)
        }

        activity?.let {
            recyclerDataAdapter = CountriesRecyclerAdapter(it, countriesRecyclerData, this)
            recyclerView.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = recyclerDataAdapter
        }

        filterByArea.setOnClickListener {
            countriesRecyclerData = viewModel.sortList(SortType.AREA, isAreaDescending, countriesRecyclerData)
            isAreaDescending = !isAreaDescending
            switchArrow(areaArrow, isAreaDescending)
            recyclerDataAdapter.refreshList(countriesRecyclerData)
        }

        filterByName.setOnClickListener {
            countriesRecyclerData = viewModel.sortList(SortType.NAME, isNameDescending, countriesRecyclerData)
            isNameDescending = !isNameDescending
            switchArrow(nameArrow, isNameDescending)
            recyclerDataAdapter.refreshList(countriesRecyclerData)
        }

        filterByPopulate.setOnClickListener {
            countriesRecyclerData = viewModel.sortList(SortType.POPULATION, isPopulationDescending, countriesRecyclerData)
            isPopulationDescending = !isPopulationDescending
            switchArrow(populateArrow, isPopulationDescending)
            recyclerDataAdapter.refreshList(countriesRecyclerData)
        }

        return layout
    }

    private fun switchArrow(view:ImageView, isDescending:Boolean){
        if (isDescending){
            view.setImageResource(R.drawable.ic_angel_up_thin)
        }else{
            view.setImageResource(R.drawable.ic_angel_down_thin)
        }

    }

    override fun onCountryClick(item: CountryObject) {
        if (item.bordersCodes?.size == 0){
            context?.let {
                val alertDialog: AlertDialog = AlertDialog.Builder(it).create()
                alertDialog.setMessage(getString(R.string.popup_message_text))
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.popup_button_text),
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                alertDialog.show()
            }

        }else{
            viewModel.getCountryBorders(item)
        }
    }
}