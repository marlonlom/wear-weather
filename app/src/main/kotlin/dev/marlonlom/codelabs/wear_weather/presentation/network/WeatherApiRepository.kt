package dev.marlonlom.codelabs.wear_weather.presentation.network

import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherApiRepository(
  private val apiService: WeatherApiService,
  private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

  fun getCurrentWeather(
    userLocation: UserLocation
  ): Flow<WeatherApiData> = flow {
    val coordinatesText = userLocation.let { "${it.latitude},${it.longitude}" }
    val weather = apiService.getCurrentWeather(coordinatesText)
    emit(weather)
  }.flowOn(coroutineDispatcher)
}
