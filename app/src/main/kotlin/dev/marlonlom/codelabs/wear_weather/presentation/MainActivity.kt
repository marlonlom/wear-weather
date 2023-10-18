/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package dev.marlonlom.codelabs.wear_weather.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
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
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.LocationPermissionDeniedException
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocation
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationState
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationViewModel
import dev.marlonlom.codelabs.wear_weather.presentation.features.main.MainScaffold
import dev.marlonlom.codelabs.wear_weather.presentation.theme.WearWeatherTheme

private val Context.dataStore by preferencesDataStore("wear_weather_preferences")

class MainActivity : ComponentActivity() {

  private val userLocationViewModel by viewModels<UserLocationViewModel> {
    UserLocationViewModel.factory(dataStore)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
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
          Log.d(
            "[MainActivity]",
            "locationPermissionsGranted=$locationPermissionsGranted"
          )
          if (!locationPermissionsGranted) {
            userLocationViewModel.updateWithFailure(LocationPermissionDeniedException())
          } else {
            /* TODO: get latest location, after that, update the ui state with found location. */
            userLocationViewModel.updateLocation(UserLocation(1.0, 1.0))
          }
        })

      val userLocationState: State<UserLocationState> = userLocationViewModel.uiState.collectAsState()

      WearWeatherTheme {
        MainScaffold(
          userLocationState = userLocationState.value
        ) {
          Log.d(
            "[MainActivity]",
            "requestLocationPermissionAction; areLocationPermissionsAlreadyGranted=${areLocationPermissionsAlreadyGranted()}"
          )
          locationPermissionLauncher.launch(locationPermissions)
        }
      }
    }
  }

  private fun areLocationPermissionsAlreadyGranted(): Boolean = ContextCompat.checkSelfPermission(
    this,
    Manifest.permission.ACCESS_FINE_LOCATION
  ) == PackageManager.PERMISSION_GRANTED
}
