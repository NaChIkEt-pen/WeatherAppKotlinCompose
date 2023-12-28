package com.nachiket.myopenweatherapi.model.weather

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") var temp : Double?= null,
    @SerializedName("feels_like") var feels_like : Double?= null,
    @SerializedName("temp_min") var temp_min : Double?= null,
    @SerializedName("temp_max") var temp_max : Double?= null,
    @SerializedName("pressure") var pressure : Int?= null,
    @SerializedName("humidity") var humidity : Int?= null,
    @SerializedName("sea_level") var sea_level : Int?= null,
    @SerializedName("grnd_level") var grnd_level : Int?= null
) {

}
