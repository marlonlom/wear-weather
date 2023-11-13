package dev.marlonlom.codelabs.wear_weather.presentation.features.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import dev.marlonlom.codelabs.wear_weather.R
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.AskingCurrentLocationScreen
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationState
import dev.marlonlom.codelabs.wear_weather.presentation.features.weather.openAppSettingsChipItem
import dev.marlonlom.codelabs.wear_weather.presentation.features.weather.retryGetWeatherChipItem
import dev.marlonlom.codelabs.wear_weather.presentation.features.weather.weatherConditionBox
import dev.marlonlom.codelabs.wear_weather.presentation.features.weather.weatherDetailsBox
import dev.marlonlom.codelabs.wear_weather.presentation.features.weather.weatherPlaceTextItem
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState
import kotlinx.coroutines.launch

@Composable
fun MainScaffold(
  userLocationState: UserLocationState,
  requestLocationPermissionAction: () -> Unit,
  openAppSettingsAction: () -> Unit,
  getCurrentWeatherAction: () -> Unit,
  weatherDataState: WeatherApiUiState
) {
  val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colors.surface),
    timeText = {
      if (!scalingLazyListState.isScrollInProgress) {
        TimeText()
      }
    },
    vignette = { Vignette(VignettePosition.TopAndBottom) },
    positionIndicator = { PositionIndicator(scalingLazyListState) }
  ) {
    val focusRequester: FocusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
      focusRequester.requestFocus()
    }

    ScalingLazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface)
        .onRotaryScrollEvent {
          coroutineScope.launch {
            scalingLazyListState.scrollBy(it.verticalScrollPixels)
          }
          true
        }
        .focusRequester(focusRequester)
        .focusable(),
      contentPadding = PaddingValues(horizontal = 10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      state = scalingLazyListState
    ) {
      Log.d("[MainScaffold]", "userLocationState=${userLocationState}")
      Log.d("[MainScaffold]", "weatherDataState=${weatherDataState}")
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
            Text(
              modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(top = 40.dp, bottom = 20.dp)
                .padding(horizontal = 10.dp),
              text = stringResource(R.string.text_general_failed_getting_location),
              textAlign = TextAlign.Center,
              style = MaterialTheme.typography.caption2
            )
          }
          openAppSettingsChipItem(openAppSettingsAction)
        }

        is UserLocationState.Located -> {

          when (weatherDataState) {
            is WeatherApiUiState.Loading -> {
              item {
                CircularProgressIndicator()
              }
            }

            is WeatherApiUiState.Failure -> {
              item {
                Text(
                  modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 40.dp, bottom = 20.dp)
                    .padding(horizontal = 10.dp),
                  text = stringResource(R.string.text_general_failed_getting_weather),
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.caption2,
                )
              }
              retryGetWeatherChipItem(
                openAppSettingsAction = { getCurrentWeatherAction() }
              )
              openAppSettingsChipItem(openAppSettingsAction)
            }

            is WeatherApiUiState.Success -> {
              weatherPlaceTextItem(weatherDataState)
              weatherConditionBox(weatherDataState)
              weatherDetailsBox(weatherDataState)
            }

            WeatherApiUiState.Empty -> {
              item {
                Text(
                  modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 40.dp, bottom = 20.dp)
                    .padding(horizontal = 10.dp),
                  text = stringResource(R.string.text_general_no_weather),
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.caption2,
                )
              }
              retryGetWeatherChipItem(
                openAppSettingsAction = { getCurrentWeatherAction() }
              )
              openAppSettingsChipItem(openAppSettingsAction)
            }

          }
        }
      }
    }
  }
}


