package com.nachiket.myopenweatherapi.model.weather

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h") var h1 : Double?= null,
)
