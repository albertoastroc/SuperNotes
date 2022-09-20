package com.gmail.pentominto.us.supernotes.optionsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OptionsScreen(
    viewModel : OptionsScreenViewModel = hiltViewModel()
) {

    Column {

        Text(
            text = "Options",
            fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp)
        )

        OptionsRow(
            optionsTitle = "Theme",
            optionSelected = "Light mode",
        ) {}


        OptionsRow(
            optionsTitle = "Note font size",
            optionSelected = "Medium",
        ) {}

        OptionsRowWithSwitch(
            optionsTitle = "Enable categories",
            optionSelected = "When disabled one uncategorized list will be displayed",
            switchState = viewModel.categoriesOptionsState.value
        ) {
            viewModel.categoriesPrefToggle()
        }
        OptionsRow(
            optionsTitle = "Export all notes",
            optionSelected = null,
        ) {}
        OptionsRow(
            optionsTitle = "Import notes",
            optionSelected = null,
        ) {}
        OptionsRow(
            optionsTitle = "Delete all notes",
            optionSelected = null,
        ) {}
        OptionsRow(
            optionsTitle = "Delete all categories",
            optionSelected = null,
        ) {}
    }
}

@Composable
fun OptionsRow(
    optionsTitle : String,
    optionSelected : String?,
    onClick : (Boolean) -> Unit
) {

    val checkedState = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
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
                text = optionsTitle,
                fontWeight = FontWeight.Bold
            )
            if (optionSelected != null) {
                Text(
                    text = optionSelected,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }

    Divider()
}

@Composable
fun OptionsRowWithSwitch(
    optionsTitle : String,
    optionSelected : String?,
    switchState : Boolean,
    onClick : (Boolean) -> Unit
) {

    val checkedState = remember { mutableStateOf( switchState ) }

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
                    start = 16.dp,
                )
                .weight(1f)
        ) {

            Text(
                text = optionsTitle,
                fontWeight = FontWeight.Bold
            )
            if (optionSelected != null) {
                Text(
                    text = optionSelected,
                    fontWeight = FontWeight.Light,
                    overflow = TextOverflow.Clip,
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
                    .padding(end = 16.dp)
            )

    }

    Divider()
}