package com.example.countriesproject.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countriesproject.SortType
import com.example.countriesproject.models.CountryObject
import com.example.countriesproject.network.CountriesInterface
import com.example.countriesproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {
    private var allCountriesCall: Call<ArrayList<CountryObject>>?
    val retrofitService: CountriesInterface?
    var steps = MutableLiveData<Steps>()
    private var currentCountry:String? = ""

    init {
        val retrofitClient: Retrofit? = RetrofitClient.createClient()
        retrofitService = retrofitClient?.create(CountriesInterface::class.java)
        allCountriesCall = retrofitService?.getAllCountries()
    }


    fun getAllCountries() {
        allCountriesCall?.enqueue(object : Callback<ArrayList<CountryObject>> {
            override fun onResponse(all: Call<ArrayList<CountryObject>>, response: Response<ArrayList<CountryObject>>) {
                response.body()?.let{
                    steps.value = Steps.DataReady(it)
                }

            }

            override fun onFailure(call: Call<ArrayList<CountryObject>>, t: Throwable) {
                steps.value = Steps.OnError
            }
        })
    }

    fun getCountryBorders(item: CountryObject) {
        currentCountry = item.name
        val bordersFormatToSearch: String = getBordersFormat(item.bordersCodes)
        val bordersCall: Call<ArrayList<CountryObject>>? =
            retrofitService?.getCountriesByCode(bordersFormatToSearch)
        bordersCall?.enqueue(object : Callback<ArrayList<CountryObject>> {

            override fun onFailure(call: Call<ArrayList<CountryObject>>, t: Throwable) {
                steps.value = Steps.OnError
            }

            override fun onResponse(call: Call<ArrayList<CountryObject>>, response: Response<ArrayList<CountryObject>>) {
                response.body()?.let{
                    steps.value = Steps.BordersReady(it, currentCountry)
                }
            }
        })
    }

    private fun getBordersFormat(borders: ArrayList<String>?): String {
        val bordersFormat: StringBuilder = StringBuilder()

        borders?.forEachIndexed { i, borderCode ->
            bordersFormat.append(borderCode)
            if (i < borders.size) {
                bordersFormat.append(";")
            }
        }

        return bordersFormat.toString()
    }

    //sorting the list by Type
    fun sortList(type: SortType, isDescending:Boolean, items:ArrayList<CountryObject>):ArrayList<CountryObject>{
        val filteredArray = ArrayList<CountryObject>()
        val sortItems :List<CountryObject>
        when(type){
            SortType.AREA -> {
                sortItems = if (isDescending){
                    items.sortedByDescending {
                        it.area
                    }
                }else{
                    items.sortedBy{
                        it.area
                    }
                }
            }
            SortType.NAME -> {
                sortItems = if (isDescending){
                    items.sortedByDescending {
                        it.name
                    }
                }else{
                    items.sortedBy{
                        it.name
                    }
                }
            }
            SortType.POPULATION -> {
                sortItems = if (isDescending){
                    items.sortedByDescending {
                        it.population
                    }
                }else{
                    items.sortedBy {
                        it.population
                    }
                }
            }
        }

        filteredArray.addAll(sortItems)
        return filteredArray
    }

    //class that contains the flow's steps
    //can add steps as necessary
    sealed class Steps() {
        class DataReady(val data: ArrayList<CountryObject>) : Steps()
        class BordersReady(val data: ArrayList<CountryObject>,val currentCountry:String?) : Steps()
        object OnError : Steps()
    }
}