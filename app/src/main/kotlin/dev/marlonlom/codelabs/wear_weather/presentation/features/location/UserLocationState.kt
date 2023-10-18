package dev.marlonlom.codelabs.wear_weather.presentation.features.location

import kotlin.math.absoluteValue

sealed class UserLocationState {
  object None : UserLocationState()
  data class Located(val userLocation: UserLocation) : UserLocationState()
  data class Failed(val exception: Throwable) : UserLocationState()
}

data class UserLocation(
  val latitude: Double,
  val longitude: Double,
) {
  val isOutOfBoundaries: Boolean
    get() = (this.latitude.absoluteValue > 90.0).and(this.longitude.absoluteValue > 180.0)
}

class LocationOutOfBoundariesException : Exception()
class LocationPermissionDeniedException : Exception()
