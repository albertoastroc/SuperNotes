@file:OptIn(ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class
)

package com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@Composable
fun NoteEditScreen(
    noteid: Int,
    viewModel: NoteEditScreenViewModel = hiltViewModel()

) {
    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val noteState by remember { viewModel.noteEditState }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = .5f)
    )

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_PAUSE
                -> {
                    viewModel.saveNoteText()
                }
                else -> {}
            }
        }

        lifeCycleOwner.addObserver(observer)

        onDispose {
            lifeCycleOwner.removeObserver(observer)
        }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = Color.Transparent,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp
                    )
            ) {
                TitleAndMenuCard(
                    customTextSelectionColors = customTextSelectionColors,
                    noteState = noteState,
                    onTitleValueChange = viewModel::onTitleInputChange,
                    setAlarm = viewModel::setAlarm,
                    coroutineScope = coroutineScope,
                    bottomSheetScaffoldState = bottomSheetScaffoldState
                )

                BodyCard(
                    customTextSelectionColors = customTextSelectionColors,
                    noteState = noteState,
                    onBodyValueChange = viewModel::onBodyInputChange
                )
            }
        },

        sheetContent = {
            CategoriesList(
                categories = noteState.categories,
                currentCategory = noteState.currentCategory,
                onAddCategory = viewModel::insertCategory,
                onDeleteCategory = viewModel::deleteCategory
            ) {
                viewModel.saveNoteCategory(it)
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }
    )
}



