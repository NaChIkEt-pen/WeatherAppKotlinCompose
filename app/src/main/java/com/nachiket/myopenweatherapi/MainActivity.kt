package com.nachiket.myopenweatherapi

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.nachiket.myopenweatherapi.constant.Const.Companion.colorBg1
import com.nachiket.myopenweatherapi.constant.Const.Companion.colorBg2
import com.nachiket.myopenweatherapi.constant.Const.Companion.permissions
import com.nachiket.myopenweatherapi.model.MyLatLong
import com.nachiket.myopenweatherapi.model.forecast.ForecastResult
import com.nachiket.myopenweatherapi.model.weather.WeatherResult
import com.nachiket.myopenweatherapi.ui.theme.MyOpenWeatherAPITheme
import com.nachiket.myopenweatherapi.viewmodel.MainViewModel
import com.nachiket.myopenweatherapi.viewmodel.STATE
import com.nachiket.myopenweatherapi.views.ForecastSection
import com.nachiket.myopenweatherapi.views.WeatherSection
import kotlinx.coroutines.coroutineScope

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var mainViewModel: MainViewModel
    private  var locationRequired: Boolean = false

    override fun onResume() {
        super.onResume()
        if (locationRequired) startLocationUpdate();
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        locationCallback?.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,100
            ).setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateAgeMillis(100)
                .build()

            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocationClient()
        initViewModel()
        setContent {
            //stroing current location
            var currentLocation by remember{
                mutableStateOf(MyLatLong(0.0,0.0))
            }

            //implementing location callback
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult){
                    super.onLocationResult(p0)
                    for(location in p0.locations){
                        currentLocation = MyLatLong(
                            location.latitude,
                            location.longitude
                        )
                    }

                    //fetchForecastInformation(mainViewModel, currentLocation)
                }
            }


            MyOpenWeatherAPITheme {
                // A surface container using the 'background' color from the theme
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    LocationScreen(this@MainActivity, currentLocation)
                }

            }
        }
    }

//    private fun fetchForecastInformation(mainViewModel: MainViewModel, currentLocation: MyLatLong) {
//        mainViewModel.state = STATE.LOADING
//        mainViewModel.getForecastByLocation(currentLocation)
//        mainViewModel.state = STATE.SUCCESS
//
//    }

    private fun fetchWeatherInformation(mainViewModel: MainViewModel, currentLocation: MyLatLong) {
        mainViewModel.state = STATE.LOADING
        mainViewModel.getWeatherByLocation(currentLocation)
        mainViewModel.getForecastByLocation(currentLocation)
        mainViewModel.state = STATE.SUCCESS
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
    }

    @Composable
    private fun LocationScreen(context: Context, currentLocation: MyLatLong) {
        //requesting runtime permissions
        val launcherMultiplePermission = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionMap ->
            val areGranted = permissionMap.values.reduce { accepted, next ->
                accepted && next
            }

            //Check all permission is accept
            if (areGranted) {
                locationRequired = true;
                startLocationUpdate();
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }

        val systemUicontoller = rememberSystemUiController()

        DisposableEffect(key1 = true, effect = {
            systemUicontoller.isSystemBarsVisible = false
            onDispose {
                systemUicontoller.isSystemBarsVisible = true
            }
        })

        LaunchedEffect(key1 = currentLocation, block = {
            coroutineScope {
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    startLocationUpdate()
                } else {
                    launcherMultiplePermission.launch(permissions)

                }
            }
        })

        LaunchedEffect(key1 = true, block ={
            fetchWeatherInformation(mainViewModel, currentLocation)
        })

        val gradient = Brush.linearGradient(
            colors = listOf(Color(colorBg1), Color(colorBg2)),
            start = Offset(1000f, -1000f),
            end = Offset(1000f, 1000f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.BottomCenter
        ) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val marginTop = screenHeight * 0f;

            val marginTopPx = with(LocalDensity.current) { marginTop.toPx()}


            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(
                            placeable.width,
                            placeable.height + marginTopPx.toInt()
                        ) {
                            placeable.placeRelative(0, marginTopPx.toInt())
                        }
                    },
                
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (mainViewModel.state) {
                    STATE.LOADING -> {
                        LoadingSection()
                    }
                    STATE.FAILED -> {
                        ErrorScreen(mainViewModel.errorMessage)
                    }
                    else -> {
                        WeatherSection(mainViewModel.weatherResponse)
                        ForecastSection(mainViewModel.forecastResponse)
                    }
                }

            }

            FloatingActionButton(
                onClick = {
                    //API fetch
                    fetchWeatherInformation(mainViewModel, currentLocation)
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Add")
            }


        }
    }





    @Composable
    fun ErrorScreen(errorMessage : String) {
         return Column(
             modifier =  Modifier.fillMaxSize(),
             verticalArrangement = Arrangement.Center,
             horizontalAlignment = Alignment.CenterHorizontally
         ) {
             Text(text = errorMessage,color = Color.White)
             Text(text = "here")
         }
    }

    @Composable
    fun LoadingSection() {
        return Column(
            modifier =  Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }


    private fun initLocationClient() {
        fusedLocationProviderClient = LocationServices.
        getFusedLocationProviderClient(this)
    }

}



