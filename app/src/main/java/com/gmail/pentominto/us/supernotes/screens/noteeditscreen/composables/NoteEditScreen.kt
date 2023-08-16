package com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberDrawerState
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
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.composables.NavigationDrawerOptionsContainer
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteEditScreen(
    noteid : Int,
    viewModel : NoteEditScreenViewModel = hiltViewModel(),
    onDrawerItemClick : (Int) -> Unit

) {
    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    val coroutineScope = rememberCoroutineScope()

    val noteState by remember { viewModel.noteEditState }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = .5f)
    )

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed),
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_STOP,
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_DESTROY
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
        drawerGesturesEnabled = true,
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            NavigationDrawerOptionsContainer(
                drawerState = bottomSheetScaffoldState.drawerState,
                onDrawerItemClick = onDrawerItemClick
            )
        },
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
                    scheduleReminder = viewModel::scheduleReminder,
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
                currentCategory = noteState.currentChosenCategory,
                onAddCategory = viewModel::insertCategory,
                onDeleteCategory = viewModel::deleteCategory
            ) {
                coroutineScope.launch {
                    viewModel.onCategoryChange(it)
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }
    )
}
