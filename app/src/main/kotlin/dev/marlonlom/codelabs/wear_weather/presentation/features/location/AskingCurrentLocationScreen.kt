package dev.marlonlom.codelabs.wear_weather.presentation.features.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import dev.marlonlom.codelabs.wear_weather.R


@Composable
fun AskingCurrentLocationScreen(
  onAskLocationPermissionButtonClicked: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp),
      textAlign = TextAlign.Center,
      text = stringResource(R.string.text_allow_location_permission),
      style = MaterialTheme.typography.caption2
    )

    CompactButton(
      onClick = {
        onAskLocationPermissionButtonClicked()
      }
    ) {
      Icon(
        painter = painterResource(R.drawable.ic_location),
        contentDescription = null,
        modifier = Modifier
          .size(24.dp)
          .wrapContentSize(align = Alignment.Center)
      )
    }
  }
}
