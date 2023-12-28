package com.nachiket.myopenweatherapi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nachiket.myopenweatherapi.model.MyLatLong
import com.nachiket.myopenweatherapi.model.forecast.ForecastResult
import com.nachiket.myopenweatherapi.model.weather.WeatherResult
import com.nachiket.myopenweatherapi.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.lang.Exception

enum class STATE{
    LOADING,
    SUCCESS,
    FAILED
}
class MainViewModel: ViewModel() {
    var state: STATE by mutableStateOf(STATE.LOADING)

    var weatherResponse : WeatherResult by mutableStateOf(WeatherResult())

    var forecastResponse : ForecastResult by mutableStateOf(ForecastResult())

    var errorMessage: String by mutableStateOf("")

    fun getWeatherByLocation(latLng: MyLatLong){
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService  = RetrofitClient.getInstance()

            try {
                val apiResponse = apiService.getWeather(latLng.Lat, latLng.Long)
                weatherResponse = apiResponse
                state = STATE.SUCCESS
            }catch (e: Exception){
                errorMessage = e.message!!.toString()
                state = STATE.FAILED
            }
        }
    }

    fun getForecastByLocation(latLng: MyLatLong){
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService  = RetrofitClient.getInstance()

            try {
                val apiResponse = apiService.getForecast(latLng.Lat, latLng.Long)
                forecastResponse = apiResponse
                state = STATE.SUCCESS
            }catch (e: Exception){
                errorMessage = e.message!!.toString()
                state = STATE.FAILED
            }
        }
    }

}