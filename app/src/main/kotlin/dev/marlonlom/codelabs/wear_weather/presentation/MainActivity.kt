/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package dev.marlonlom.codelabs.wear_weather.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.LocationNotFoundException
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.LocationPermissionDeniedException
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocation
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationState
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationViewModel
import dev.marlonlom.codelabs.wear_weather.presentation.features.main.MainScaffold
import dev.marlonlom.codelabs.wear_weather.presentation.network.CurrentWeatherViewModel
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiClient
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiRepository
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState
import dev.marlonlom.codelabs.wear_weather.presentation.theme.WearWeatherTheme

private val Context.dataStore by preferencesDataStore("wear_weather_preferences")

class MainActivity : ComponentActivity() {

  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var locationRequest: LocationRequest
  private val locationCallback = object : LocationCallback() {}

  private val userLocationViewModel by viewModels<UserLocationViewModel> {
    UserLocationViewModel.factory(dataStore)
  }

  private val currentWeatherViewModel by viewModels<CurrentWeatherViewModel> {
    CurrentWeatherViewModel.factory(
      WeatherApiRepository(WeatherApiClient.getInstance())
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    Log.d("[MainActivity]", "onCreate")
    installSplashScreen()
    super.onCreate(savedInstanceState)

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1)
      .apply {
        setIntervalMillis(0L)
        setMinUpdateIntervalMillis(0L)
        setMaxUpdates(1)
      }
      .build()
    requestLocationChecks()

    setContent {

      var locationPermissionsGranted by remember {
        mutableStateOf(areLocationPermissionsAlreadyGranted())
      }

      val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
      )

      val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
          locationPermissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
            acc && isPermissionGranted
          }
          if (!locationPermissionsGranted) {
            Log.d(
              "[MainActivity]",
              "locationPermissionLauncher.denied = LocationPermissionDeniedException"
            )
            userLocationViewModel.updateWithFailure(
              LocationPermissionDeniedException()
            )
          } else {
            getCurrentLocation()
          }
        })

      val userLocationState: State<UserLocationState> = userLocationViewModel.uiState.collectAsState()
      val weatherDataState: State<WeatherApiUiState> = currentWeatherViewModel.weatherData.collectAsState()

      val currentLocationData = userLocationState.value
      if (currentLocationData is UserLocationState.Located) {
        currentWeatherViewModel.updateWeatherDataByLocation(currentLocationData.userLocation)
      }

      WearWeatherTheme {
        MainScaffold(
          userLocationState = currentLocationData,
          requestLocationPermissionAction = {
            Log.d(
              "[MainActivity]",
              "requestLocationPermissionAction; areLocationPermissionsAlreadyGranted=${areLocationPermissionsAlreadyGranted()}"
            )
            if (areLocationPermissionsAlreadyGranted()) {
              getCurrentLocation()
            } else {
              locationPermissionLauncher.launch(locationPermissions)
            }
          },
          weatherDataState = weatherDataState.value,
        )
      }

    }
  }

  override fun onStop() {
    super.onStop()
    Log.d("[MainActivity]", "onStop")
    fusedLocationClient.removeLocationUpdates(locationCallback)
  }

  @SuppressLint("MissingPermission")
  private fun requestLocationChecks() {
    Log.d("[MainActivity]", "requestLocationChecks")
    fusedLocationClient.requestLocationUpdates(
      locationRequest,
      locationCallback,
      Looper.myLooper()
    )
  }

  private fun getCurrentLocation() {
    handleCurrentLocation(
      onLocationFound = { location ->
        val userLocation = UserLocation(location.latitude, location.longitude)
        userLocationViewModel.updateLocation(userLocation)
        currentWeatherViewModel.updateWeatherDataByLocation(userLocation)
      },
      onFailure = {
        Log.d(
          "[MainActivity]",
          "getCurrentLocation.failure = ${it.javaClass.simpleName}"
        )
        userLocationViewModel.updateWithFailure(it)
      }
    )
  }

  @SuppressLint("MissingPermission")
  private fun handleCurrentLocation(
    onLocationFound: (Location) -> Unit,
    onFailure: (Throwable) -> Unit
  ) {
    if (areLocationPermissionsAlreadyGranted()) {
      fusedLocationClient.lastLocation
        .addOnSuccessListener {
          if (it != null) {
            onLocationFound(it)
          } else {
            onFailure(LocationNotFoundException())
          }
        }
        .addOnFailureListener { exception ->
          onFailure(exception)
        }
    }
  }

  private fun areLocationPermissionsAlreadyGranted(): Boolean = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
  ).all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
  }
}
