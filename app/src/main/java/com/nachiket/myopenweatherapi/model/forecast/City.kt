package com.nachiket.myopenweatherapi.model.forecast

import com.google.gson.annotations.SerializedName
import com.nachiket.myopenweatherapi.model.weather.Coord

data class City(
    @SerializedName("id") var id : Int?= null,
    @SerializedName("name") var name: String?=null,
    @SerializedName("coord") var coord: Coord?= Coord(),
    @SerializedName("country") var country: String?= null,
    @SerializedName("population") var population: Int?= null,
    @SerializedName("timezone") var timezone: Int?= null,
    @SerializedName("sunrise") var sunrise: Int?= null,
    @SerializedName("sunset") var sunset: Int?= null,

)
