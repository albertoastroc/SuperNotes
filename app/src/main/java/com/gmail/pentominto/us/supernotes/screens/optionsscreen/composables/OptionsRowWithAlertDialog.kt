package com.gmail.pentominto.us.supernotes.screens.optionsscreen.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OptionsRowWithAlertDialog(
    title : String,
    subTitle : String?,
    message : String,
    yesButtonMessage : String,
    noButtonMessage : String,
    onClick : () -> Unit
) {
    val openConfirmDialog = remember { mutableStateOf(false) }

    if (openConfirmDialog.value) {
        AlertDialog(
            backgroundColor = MaterialTheme.colors.primary,
            onDismissRequest = { openConfirmDialog.value = false },
            title = { Text(text = message) },
            confirmButton = {
                Button(
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                    onClick = {
                        onClick()
                        openConfirmDialog.value = false
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = yesButtonMessage)
                }
            },
            dismissButton = {
                Button(
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                    onClick = {
                        openConfirmDialog.value = false
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = noButtonMessage)
                }
            }

        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                openConfirmDialog.value = true
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    bottom = 24.dp,
                    start = 16.dp
                )
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
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }

    Divider()
}
