package dev.marlonlom.codelabs.wear_weather.presentation.features.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Air
import androidx.compose.material.icons.twotone.Cloud
import androidx.compose.material.icons.twotone.Speed
import androidx.compose.material.icons.twotone.Thunderstorm
import androidx.compose.material.icons.twotone.WaterDrop
import androidx.compose.material.icons.twotone.WbSunny
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import dev.marlonlom.codelabs.wear_weather.R
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState
import kotlin.math.roundToLong

fun ScalingLazyListScope.weatherPlaceTextItem(
  weatherDataState: WeatherApiUiState.Success
) {
  item {
    Text(
      modifier = Modifier.fillMaxWidth(), text = buildAnnotatedString {
        withStyle(
          SpanStyle(
            fontSize = MaterialTheme.typography.title3.fontSize,
            fontFamily = MaterialTheme.typography.title3.fontFamily,
          )
        ) {
          append(weatherDataState.data.location!!.name)
        }
        append("\n")
        withStyle(
          SpanStyle(
            fontSize = MaterialTheme.typography.caption3.fontSize,
            fontFamily = MaterialTheme.typography.caption3.fontFamily,
          )
        ) {
          append(weatherDataState.data.location!!.country)
        }
      }, textAlign = TextAlign.Center
    )
  }
}

fun ScalingLazyListScope.weatherConditionBox(
  weatherDataState: WeatherApiUiState.Success
) {
  item {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      CurrentWeatherValueText(weatherDataState.data.current!!.tempC!!)
      WeatherConditionAsyncImage(weatherDataState)
      CurrentWeatherValueText(weatherDataState.data.current!!.feelsLikeCelsius!!, itFeelsLike = true)
    }
  }
  item {
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 40.dp),
      text = weatherDataState.data.current!!.condition!!.text!!,
      textAlign = TextAlign.Center,
      maxLines = 2,
      color = MaterialTheme.colors.secondary,
      style = MaterialTheme.typography.caption2
    )
  }
}

fun ScalingLazyListScope.weatherDetailsBox(
  weatherDataState: WeatherApiUiState.Success
) {
  item {
    WeatherDetailOutlinedChip(
      iconImageVector = Icons.TwoTone.WaterDrop,
      labelText = "${weatherDataState.data.current!!.humidity!!.roundToLong()} %",
      secondaryLabelText = stringResource(R.string.text_general_humidity)
    )
  }

  item {
    WeatherDetailOutlinedChip(
      iconImageVector = Icons.TwoTone.Air,
      labelText = weatherDataState.data.current!!.let {
        "${it.windDir} ${it.windKph!!.roundToLong()} km/h"
      },
      secondaryLabelText = stringResource(R.string.text_general_wind_speed)
    )
  }

  item {
    WeatherDetailOutlinedChip(
      iconImageVector = Icons.TwoTone.Speed,
      labelText = "${weatherDataState.data.current!!.pressureMb!!.roundToLong()} mb",
      secondaryLabelText = stringResource(R.string.text_general_pressure)
    )
  }

  item {
    WeatherDetailOutlinedChip(
      iconImageVector = Icons.TwoTone.Cloud,
      labelText = "${weatherDataState.data.current!!.cloud!!.roundToLong()} %",
      secondaryLabelText = stringResource(R.string.text_general_cloud_cover)
    )
  }

  item {
    WeatherDetailOutlinedChip(
      iconImageVector = Icons.TwoTone.Thunderstorm,
      labelText = "${weatherDataState.data.current!!.precipitationMm!!.roundToLong()} millimeters",
      secondaryLabelText = stringResource(R.string.text_general_precipitation)
    )
  }

  item {
    WeatherDetailOutlinedChip(
      iconImageVector = Icons.TwoTone.Air,
      labelText = "${weatherDataState.data.current!!.gustKph!!.roundToLong()} km/h",
      secondaryLabelText = stringResource(R.string.text_general_wind_gust)
    )
  }

  item {
    WeatherDetailOutlinedChip(
      iconImageVector = Icons.TwoTone.WbSunny,
      labelText = weatherDataState.data.current!!.let {
        val uvIndexScaleName = stringArrayResource(id = R.array.texts_uv_indexes)[it.uvIndex.ordinal]
        "${it.uv!!.roundToLong()}: $uvIndexScaleName"
      },
      secondaryLabelText = stringResource(R.string.text_general_uv_index)
    )
  }
}
