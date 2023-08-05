package com.gmail.pentominto.us.supernotes.screens.optionsscreen.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun OptionsRowWithSwitch(
    title: String,
    subTitle: String?,
    switchState: Boolean,
    onClick: (Boolean) -> Unit
) {
    val checkedState = remember { mutableStateOf(switchState) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    bottom = 24.dp,
                    start = 16.dp
                )
                .weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground
            )
            if (subTitle != null) {
                Text(
                    text = subTitle,
                    fontWeight = FontWeight.Light,
                    overflow = TextOverflow.Clip,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
        Switch(
            checked = checkedState.value,
            onCheckedChange = {
                onClick(checkedState.value)
                checkedState.value = it
            },
            modifier = Modifier
                .padding(end = 16.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.secondary,
                uncheckedTrackColor = MaterialTheme.colors.secondary,
                checkedTrackAlpha = 1f,
                uncheckedTrackAlpha = .5f
            )
        )
    }

    Divider()
}
