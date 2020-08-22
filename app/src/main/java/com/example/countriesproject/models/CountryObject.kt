package com.example.countriesproject.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryObject(
    val name:String?,
    val nativeName:String?,
    @SerializedName("alpha3Code")
    val alphaCode:String?,
    @SerializedName("flag")
    val flagPhotoUrl:String?,
    @SerializedName("borders")
    val bordersCodes:ArrayList<String>?,
    val area:Double?,
    val population:Int?
):Parcelable


