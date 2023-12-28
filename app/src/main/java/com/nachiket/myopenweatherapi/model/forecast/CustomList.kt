package com.nachiket.myopenweatherapi.model.forecast


import com.google.gson.annotations.SerializedName
import com.nachiket.myopenweatherapi.model.weather.Clouds
import com.nachiket.myopenweatherapi.model.weather.Main
import com.nachiket.myopenweatherapi.model.weather.Sys
import com.nachiket.myopenweatherapi.model.weather.Weather
import com.nachiket.myopenweatherapi.model.weather.Wind

data class CustomList(
    @SerializedName("dt") var dt : Int?= null,
    @SerializedName("main") var main : Main?= Main(),
    @SerializedName("weather") var weather: ArrayList<Weather> ?= arrayListOf(),
    @SerializedName("clouds") var clouds: Clouds ?= Clouds(),
    @SerializedName("wind") var wind: Wind ?= Wind(),
    @SerializedName("visibility") var Visibility: Int ?= null,
    @SerializedName("pop") var pop: Double ?= null,
    @SerializedName("sys") var sys: Sys ?= Sys(),
    @SerializedName("dx_txt") var dttxt: Double ?= null,

)
