package dev.marlonlom.codelabs.wear_weather.presentation.features.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocation
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationState
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationViewModel

@Composable
fun MainScaffold(
  userLocationViewModel: UserLocationViewModel
) {
  val userLocationState = userLocationViewModel.uiState.collectAsState()
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
      Log.d("[MainScaffold]", "userLocationState=${userLocationState.value}")
      when (userLocationState.value) {
        UserLocationState.None -> {
          item {
            AskingCurrentLocationScreen(
              onAskLocationPermissionButtonClicked = {
                Log.d("[MainScaffold]", "onAskLocationPermissionButtonClicked")
                userLocationViewModel.updateLocation(UserLocation(1.0, 20.0))
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
            val userLocation = userLocationState.value as UserLocationState.Located
            Text(text = "Success location: ${userLocation.userLocation}")
          }
        }
      }
    }
  }
}
