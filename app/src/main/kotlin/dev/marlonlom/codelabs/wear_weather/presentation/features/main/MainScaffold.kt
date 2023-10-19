package dev.marlonlom.codelabs.wear_weather.presentation.features.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.AskingCurrentLocationScreen
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationState
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState

@Composable
fun MainScaffold(
  userLocationState: UserLocationState,
  requestLocationPermissionAction: () -> Unit,
  weatherDataState: WeatherApiUiState
) {
  val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
  Scaffold(
    timeText = { TimeText() },
    vignette = { Vignette(VignettePosition.TopAndBottom) },
    positionIndicator = { PositionIndicator(scalingLazyListState) }
  ) {
    ScalingLazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface),
      contentPadding = PaddingValues(horizontal = 10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      state = scalingLazyListState
    ) {
      Log.d("[MainScaffold]", "userLocationState=${userLocationState}")
      when (userLocationState) {
        UserLocationState.None -> {
          item {
            AskingCurrentLocationScreen(
              onAskLocationPermissionButtonClicked = {
                Log.d("[MainScaffold]", "onAskLocationPermissionButtonClicked")
                requestLocationPermissionAction()
              }
            )
          }
        }

        is UserLocationState.Failed -> {
          item {
            Text(text = "Failed to get location")
          }
        }

        is UserLocationState.Located -> {
          item {
            Log.d("[MainScaffold]", "weatherDataState=$weatherDataState")
            Text(text = "Success location: ${userLocationState.userLocation}")
          }
        }
      }
    }
  }
}
