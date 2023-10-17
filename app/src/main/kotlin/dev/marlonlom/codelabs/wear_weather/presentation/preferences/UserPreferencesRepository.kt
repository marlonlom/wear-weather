package dev.marlonlom.codelabs.wear_weather.presentation.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
  private val dataStore: DataStore<Preferences>
) {

  private object PreferencesKeys {
    val USER_LATITUDE = doublePreferencesKey("user_latitude")
    val USER_LONGITUDE = doublePreferencesKey("user_longitude")
  }

  val userPreferencesFlow: Flow<UserLocation?> = dataStore.data.map { preferences ->
    mapUserPreferences(preferences)
  }

  suspend fun updateLocation(userLocation: UserLocation) {
    dataStore.edit { preferences ->
      preferences[PreferencesKeys.USER_LATITUDE] = userLocation.latitude
      preferences[PreferencesKeys.USER_LONGITUDE] = userLocation.longitude
    }
  }

  private fun mapUserPreferences(
    preferences: Preferences
  ): UserLocation? = if (preferences.asMap().isNotEmpty()) {
    Log.d("[UserPreferencesRepository]", "preferences=${preferences.asMap()}")
    UserLocation(
      latitude = preferences[PreferencesKeys.USER_LATITUDE] ?: 90.1,
      longitude = preferences[PreferencesKeys.USER_LONGITUDE] ?: 180.1,
    )
  } else null
}
