package dev.marlonlom.codelabs.wear_weather.presentation.features.location

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.codelabs.wear_weather.presentation.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserLocationViewModel(
  private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

  val uiState: StateFlow<UserLocationState> = userPreferencesRepository.userPreferencesFlow
    .catch { cause: Throwable ->
      Log.d("[UserLocationViewModel]", "error=$cause")
      UserLocationState.Failed(cause)
    }
    .map { userLocation ->
      when (userLocation) {
        null -> {
          UserLocationState.None
        }

        else -> {
          Log.d("[UserLocationViewModel]", "userLocation.isOutOfBoundaries=${userLocation.isOutOfBoundaries}")
          if (userLocation.isOutOfBoundaries) {
            UserLocationState.Failed(LocationOutOfBoundariesException())
          } else {
            UserLocationState.Located(userLocation)
          }
        }
      }
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.Eagerly,
      initialValue = UserLocationState.None
    )

  fun updateLocation(userLocation: UserLocation) {
    viewModelScope.launch {
      userPreferencesRepository.updateLocation(userLocation)
    }
  }

  companion object {
    fun factory(dataStore: DataStore<Preferences>) = object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserLocationViewModel::class.java)) {
          @Suppress("UNCHECKED_CAST")
          return UserLocationViewModel(UserPreferencesRepository(dataStore)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
      }
    }
  }
}
