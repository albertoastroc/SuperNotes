package com.gmail.pentominto.us.supernotes.screens.optionsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        Text(
            text = "Options",
            fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp),
            color = MaterialTheme.colors.onBackground
        )


        OptionsRowWithSwitch(
            title = "Dark mode",
            subTitle = "App specific theme",
            viewModel.optionsScreenState.value.darkThemeOption
        ) {
            viewModel.themeToggle()
        }

        OptionsRowWithSwitch(
            title = "Enable categories",
            subTitle = "When disabled one uncategorized list will be shown",
            switchState = viewModel.optionsScreenState.value.categoriesOption
        ) {
            viewModel.categoriesPrefToggle()
        }

        OptionsRowWithSwitch(
            title = "Enable Trash folder",
            subTitle = "When disabled notes will be permanently deleted when swiped off the home screen",
            switchState = viewModel.optionsScreenState.value.trashEnabled
        ) {
            viewModel.trashFolderToggle()
        }

//        OptionsRowWithAlertDialog(
//            title = "Export all notes",
//            subTitle = "You can export individual notes in the edit note screen",
//            message = "null"
//        ) {
//
//        }
//        OptionsRowWithAlertDialog(
//            title = "Import notes",
//            subTitle = null,
//            message = "null"
//        ) {
//
//        }
        OptionsRowWithAlertDialog(
            title = "Delete home screen notes",
            subTitle = "Deleting this way will not send notes to the Trash folder",
            message = "Are you sure you want to delete all notes?"
        ) {
            viewModel.deleteAllNotes()
        }
        OptionsRowWithAlertDialog(
            title = "Delete notes in Trash folder",
            subTitle = null,
            message = "Are you sure you want to delete all notes in Trash?"
        ) {
            viewModel.deleteAllTrashNotes()
        }
    }
}

@Composable
fun OptionsRowWithAlertDialog(
    title : String,
    subTitle : String?,
    message : String,
    onClick : () -> Unit,
) {

    val openConfirmDialog = remember { mutableStateOf(false) }

    if (openConfirmDialog.value) {

        AlertDialog(onDismissRequest = { openConfirmDialog.value = false },
            title = { Text(text = message) },
            confirmButton = {
                Button(
                    onClick = {
                        onClick()
                        openConfirmDialog.value = false
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openConfirmDialog.value = false
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Cancel")
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

@Composable
fun OptionsRowWithSwitch(
    title : String,
    subTitle : String?,
    switchState : Boolean,
    onClick : (Boolean) -> Unit
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
                    start = 16.dp,
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
                checkedTrackColor = MaterialTheme.colors.primaryVariant,
                uncheckedTrackColor = MaterialTheme.colors.primaryVariant,
                checkedTrackAlpha = 1f,
                uncheckedTrackAlpha = 1f,
            )
        )
    }

    Divider()
}