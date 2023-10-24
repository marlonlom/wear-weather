package dev.marlonlom.codelabs.wear_weather.presentation.features.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import dev.marlonlom.codelabs.wear_weather.R
import kotlin.math.roundToLong

@Composable
fun CurrentWeatherValueText(
  weatherValue: Double,
  weatherUnit: String = "C",
  itFeelsLike: Boolean = false
) {
  Text(
    text = buildAnnotatedString {
      val weatherValueFontSize = if (itFeelsLike) {
        MaterialTheme.typography.caption2.fontSize
      } else {
        MaterialTheme.typography.body1.fontSize
      }
      withStyle(
        SpanStyle(
          fontSize = weatherValueFontSize,
          fontFamily = MaterialTheme.typography.body1.fontFamily,
        )
      ) {
        append("${weatherValue.roundToLong()}° $weatherUnit")
      }
      append("\n")
      if (itFeelsLike) {
        withStyle(
          SpanStyle(
            fontSize = MaterialTheme.typography.caption3.fontSize,
            fontFamily = MaterialTheme.typography.caption3.fontFamily,
          )
        ) {
          append(stringResource(R.string.text_weather_feels_like))
        }
      }
    },
    textAlign = TextAlign.Center,
    style = MaterialTheme.typography.caption3
  )
}
