package dev.marlonlom.codelabs.wear_weather.presentation.network


interface UvIndexScalable {
  fun isInScale(uv: Double): Boolean
}

enum class UvIndex : UvIndexScalable {
  LOW {
    override fun isInScale(uv: Double): Boolean = uv >= 0.0 && uv < 3.0
  },
  MODERATE {
    override fun isInScale(uv: Double): Boolean = uv >= 3.0 && uv < 6.0
  },
  HIGH {
    override fun isInScale(uv: Double): Boolean = uv >= 6.0 && uv < 8.0
  },
  VERY_HIGH {
    override fun isInScale(uv: Double): Boolean = uv >= 8.0 && uv < 11.0
  },
  EXTREME {
    override fun isInScale(uv: Double): Boolean = uv >= 11.0
  };

  companion object {
    fun from(uv: Double): UvIndex = values().find { uvi -> uvi.isInScale(uv) } ?: LOW
  }
}
