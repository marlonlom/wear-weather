package dev.marlonlom.codelabs.wear_weather.presentation.features.weather

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.OutlinedChip
import androidx.wear.compose.material.Text
import dev.marlonlom.codelabs.wear_weather.R

fun ScalingLazyListScope.openAppSettingsChipItem(
  openAppSettingsAction: () -> Unit
) {
  item {
    Chip(
      modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
      onClick = {
        openAppSettingsAction()
      },
      colors = ChipDefaults.chipColors(
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        contentColor = MaterialTheme.colors.secondary,
      ),
      label = {
        Text(
          text = stringResource(R.string.text_check_settings),
          maxLines = 1,
          style = MaterialTheme.typography.caption2
        )
      },
      icon = {
        Icon(
          imageVector = Icons.TwoTone.Settings,
          contentDescription = null
        )
      },
    )
  }
}

fun ScalingLazyListScope.retryGetWeatherChipItem(
  openAppSettingsAction: () -> Unit
) {
  item {
    Chip(
      modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
      onClick = {
        openAppSettingsAction()
      },
      colors = ChipDefaults.chipColors(
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        contentColor = MaterialTheme.colors.secondary,
      ),
      label = {
        Text(
          text = stringResource(R.string.text_general_retry),
          maxLines = 1,
          style = MaterialTheme.typography.caption2
        )
      },
      icon = {
        Icon(
          imageVector = Icons.TwoTone.Refresh,
          contentDescription = null
        )
      },
    )
  }
}

@Composable
fun WeatherDetailOutlinedChip(
  iconImageVector: ImageVector,
  labelText: String,
  secondaryLabelText: String
) {
  OutlinedChip(
    colors = ChipDefaults.chipColors(
      backgroundColor = MaterialTheme.colors.surface,
      contentColor = MaterialTheme.colors.onSurface
    ),
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    icon = {
      Icon(
        imageVector = iconImageVector,
        contentDescription = null
      )
    },
    label = {
      Text(
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        style = MaterialTheme.typography.caption2,
        text = labelText
      )
    },
    secondaryLabel = {
      Text(
        text = secondaryLabelText,
        style = MaterialTheme.typography.caption3
      )
    },
    onClick = { }
  )
}
