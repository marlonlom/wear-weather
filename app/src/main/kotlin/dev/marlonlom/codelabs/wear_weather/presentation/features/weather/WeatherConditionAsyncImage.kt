package dev.marlonlom.codelabs.wear_weather.presentation.features.weather

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.marlonlom.codelabs.wear_weather.R
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState

@Composable
fun WeatherConditionAsyncImage(weatherDataState: WeatherApiUiState.Success) {
  val iconUrl = "https:".plus(weatherDataState.data.current!!.condition!!.icon!!)
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(iconUrl)
      .crossfade(true)
      .build(),
    contentScale = ContentScale.Crop,
    contentDescription = null,
    placeholder = painterResource(R.drawable.ic_pending),
    modifier = Modifier.size(48.dp)
  )
}
