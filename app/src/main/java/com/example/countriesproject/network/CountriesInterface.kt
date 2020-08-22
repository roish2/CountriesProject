package com.example.countriesproject.network

import com.example.countriesproject.models.CountryObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountriesInterface {

    @GET("all")
    fun getAllCountries(): Call<ArrayList<CountryObject>>?

    @GET("alpha")
    fun getCountriesByCode(@Query("codes")codes:String): Call<ArrayList<CountryObject>>?
}