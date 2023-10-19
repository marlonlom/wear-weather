package dev.marlonlom.codelabs.wear_weather.presentation.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.LocationOutOfBoundariesException
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocation
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState.Empty
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState.Failure
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState.Loading
import dev.marlonlom.codelabs.wear_weather.presentation.network.WeatherApiUiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
  private val repository: WeatherApiRepository
) : ViewModel() {

  private var _weatherData: MutableStateFlow<WeatherApiUiState> = MutableStateFlow(Empty)

  val weatherData: StateFlow<WeatherApiUiState> = _weatherData.stateIn(
    scope = viewModelScope,
    started = SharingStarted.Eagerly,
    initialValue = Empty
  )

  fun updateWeatherDataByLocation(userLocation: UserLocation) {
    viewModelScope.launch {
      with(_weatherData) {
        emit(Loading)
        if (userLocation.isOutOfBoundaries) {
          emit(Failure(LocationOutOfBoundariesException()))
        } else {
          repository.getCurrentWeather(userLocation)
            .catch { cause -> emit(Failure(cause)) }
            .collect { weatherApiData -> emit(Success(weatherApiData)) }
        }
      }
    }
  }

  companion object {

    fun factory(repository: WeatherApiRepository) = object : ViewModelProvider.Factory {

      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
          @Suppress("UNCHECKED_CAST")
          return CurrentWeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
      }

    }

  }

}
