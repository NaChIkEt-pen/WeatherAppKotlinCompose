package com.nachiket.myopenweatherapi.model.weather

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") var all : Int?= null,
)
