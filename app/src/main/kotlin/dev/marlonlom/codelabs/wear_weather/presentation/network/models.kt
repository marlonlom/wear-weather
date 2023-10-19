package dev.marlonlom.codelabs.wear_weather.presentation.network

import com.google.gson.annotations.SerializedName

data class WeatherApiData(
  @SerializedName("location") var location: Location? = Location(),
  @SerializedName("current") var current: Current? = Current()
)

data class Location(
  @SerializedName("name") var name: String? = null,
  @SerializedName("region") var region: String? = null,
  @SerializedName("country") var country: String? = null,
  @SerializedName("lat") var lat: Double? = null,
  @SerializedName("lon") var lon: Double? = null,
  @SerializedName("tz_id") var tzId: String? = null,
  @SerializedName("localtime_epoch") var localtimeEpoch: Int? = null,
  @SerializedName("localtime") var localtime: String? = null
)

data class Condition(
  @SerializedName("text") var text: String? = null,
  @SerializedName("icon") var icon: String? = null,
  @SerializedName("code") var code: Int? = null
)

data class Current(
  @SerializedName("last_updated_epoch") var lastUpdatedEpoch: Int? = null,
  @SerializedName("last_updated") var lastUpdated: String? = null,
  @SerializedName("temp_c") var tempC: Int? = null,
  @SerializedName("temp_f") var tempF: Double? = null,
  @SerializedName("is_day") var isDay: Int? = null,
  @SerializedName("condition") var condition: Condition? = Condition(),
  @SerializedName("wind_mph") var windMph: Double? = null,
  @SerializedName("wind_kph") var windKph: Double? = null,
  @SerializedName("wind_degree") var windDegree: Int? = null,
  @SerializedName("wind_dir") var windDir: String? = null,
  @SerializedName("pressure_mb") var pressureMb: Int? = null,
  @SerializedName("pressure_in") var pressureIn: Double? = null,
  @SerializedName("precip_mm") var precipitationMm: Double? = null,
  @SerializedName("precip_in") var precipitationIn: Double? = null,
  @SerializedName("humidity") var humidity: Int? = null,
  @SerializedName("cloud") var cloud: Int? = null,
  @SerializedName("feelslike_c") var feelsLikeCelsius: Double? = null,
  @SerializedName("feelslike_f") var feelsLikeFahrenheit: Double? = null,
  @SerializedName("vis_km") var visKm: Int? = null,
  @SerializedName("vis_miles") var visMiles: Int? = null,
  @SerializedName("uv") var uv: Int? = null,
  @SerializedName("gust_mph") var gustMph: Double? = null,
  @SerializedName("gust_kph") var gustKph: Double? = null
)

sealed class WeatherApiUiState {
  object Empty : WeatherApiUiState()
  object Loading : WeatherApiUiState()
  data class Failure(val cause: Throwable) : WeatherApiUiState()
  data class Success(val data: WeatherApiData) : WeatherApiUiState()
}
