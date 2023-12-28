package com.nachiket.myopenweatherapi.constant

import coil.compose.AsyncImagePainter

class Const {
    companion object{
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        const val openWeatherMapApiKey = "a95422a5f0c276643420465731020d65";

        const val colorBg1= 0xff08203e;
        const val colorBg2 = 0xff557c93;
        const val cardColor = 0xFF333639;

        const val LOADING = "Loading ..."
        const val NA = "N/A"
    }
}