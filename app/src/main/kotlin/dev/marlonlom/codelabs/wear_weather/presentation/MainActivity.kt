/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package dev.marlonlom.codelabs.wear_weather.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import dev.marlonlom.codelabs.wear_weather.presentation.features.location.UserLocationViewModel
import dev.marlonlom.codelabs.wear_weather.presentation.features.main.MainScaffold
import dev.marlonlom.codelabs.wear_weather.presentation.theme.WearWeatherTheme

private val Context.dataStore by preferencesDataStore("wear_weather_preferences")

class MainActivity : ComponentActivity() {

  private val userLocationViewModel by viewModels<UserLocationViewModel> {
    UserLocationViewModel.factory(dataStore)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
    setContent {
      WearWeatherTheme {
        MainScaffold(userLocationViewModel)
      }
    }
  }
}
