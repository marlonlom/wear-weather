package dev.marlonlom.codelabs.wear_weather.presentation.network

import androidx.compose.ui.text.intl.Locale
import dev.marlonlom.codelabs.wear_weather.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

  @GET("v1/current.json")
  suspend fun getCurrentWeather(
    @Query("q") query: String,
    @Query("aqi") aqi: String = "no",
    @Query("lang") language: String = Locale.current.language,
    @Query("key") apiKey: String = BuildConfig.WEATHER_API_KEY
  ): WeatherApiData
}

class WeatherApiClient {

  companion object {
    private var instance: WeatherApiService? = null

    @Synchronized
    fun getInstance(): WeatherApiService {
      if (instance == null)
        instance = Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create())
          .baseUrl(BuildConfig.WEATHER_API_BASE_URL)
          .build()
          .create(WeatherApiService::class.java)
      return instance as WeatherApiService
    }
  }
}
