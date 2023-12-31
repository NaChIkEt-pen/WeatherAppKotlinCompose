package com.nachiket.myopenweatherapi.views

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nachiket.myopenweatherapi.constant.Const.Companion.LOADING
import com.nachiket.myopenweatherapi.constant.Const.Companion.NA
import com.nachiket.myopenweatherapi.model.weather.WeatherResult
import com.nachiket.myopenweatherapi.utils.Utils.Companion.buildIcon
import com.nachiket.myopenweatherapi.utils.Utils.Companion.timeStampToHumanData

import com.nachiket.myopenweatherapi.viewmodel.STATE
import java.text.DecimalFormat
import kotlin.reflect.typeOf

@Composable
fun WeatherSection(weatherResponse: WeatherResult) {
    //title section
    var title = ""
    if (!weatherResponse.name.isNullOrEmpty()){
        weatherResponse?.name?.let {
            title = it
        }
    }
    else{
        weatherResponse.coord?.let { {
            title = "${it.lat}/${it.lon}"
        } }
    }

    //sub title section
    var subTitle = ""
    val dataVal = (weatherResponse.dt ?: 0)
    subTitle = if(dataVal == 0 ) LOADING
    else timeStampToHumanData(dataVal.toLong(), "dd-MM-yyyy")

    //icon
    var icon = ""
    var description = ""
    weatherResponse.weather.let{
        if(it!!.size>0){
            description = if(it[0].description == null ) LOADING else it[0].description!!
            icon = if(it[0].icon == null) LOADING else it[0].icon!!
        }
    }
    //temp
    var temp = ""
    weatherResponse.main?.let {


        temp = if(it.temp == null) LOADING else "${DecimalFormat("#.##")?.format((it?.temp)?.minus(275.00))} °C"
        //temp = "${DecimalFormat("#.##")?.format((it?.temp)?.minus(275.00))} °C"


    }
    //wind
    var wind=""
    weatherResponse.wind.let {
        wind = if(it == null) LOADING else "${it.speed}"
    }
    //clouds
    var clouds=""
    weatherResponse.clouds.let {
        clouds = if(it == null) LOADING else "${it.all}"
    }
    //snow
    var snow=""
    weatherResponse.snow.let {
        snow = if(it!!.d1h == null) "${NA}" else "${it.d1h}"
    }


    WeatherTitleSection(text= title, subText = subTitle , fontSize = 30.sp)
    WeatherImage(icon = icon)
    WeatherTitleSection(text= temp, subText = description , fontSize = 60.sp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround

    ){
        WeatherInfo("Wind",text = wind)
        WeatherInfo("Coulds",text = clouds)
        WeatherInfo("Snow",text = snow)
    }



//    return Column(
//        modifier =  Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = weatherResponse.toString())
//    }

}

@Composable
fun WeatherInfo(name: String, text: String) {
    Column {

        Text(text = name, fontSize = 24.sp, color = Color.White)
        Text(text= text, fontSize = 24.sp, color = Color.White)
    }

}


@Composable
fun WeatherImage(icon: String) {
    AsyncImage(model = buildIcon(icon), contentDescription = icon ,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp),
        contentScale = ContentScale.FillBounds
            )

}

@Composable
fun WeatherTitleSection(text: String, subText: String, fontSize: TextUnit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "By Nachiket")
        Text(text, fontSize = fontSize, color = Color.White, fontWeight = FontWeight.Bold )
        Text(subText, fontSize = 14.sp , color = Color.White )
    }
}
