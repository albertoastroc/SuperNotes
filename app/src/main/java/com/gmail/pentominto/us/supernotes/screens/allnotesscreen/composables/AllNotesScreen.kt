@file:OptIn(ExperimentalFoundationApi::class)

package com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.AllNotesViewModel

@Composable
fun AllNotesScreen(
    viewModel: AllNotesViewModel = hiltViewModel(),
    onNoteClick: (Int) -> Unit,
    onOptionsClick: (Int) -> Unit
) {
    val allNotesState by remember { viewModel.allNotesState }

    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            AllNotesDrawer(
                coroutineScope,
                scaffoldState,
                onOptionsClick
            )
        },
        content = { paddingValues ->

            NotesList(
                paddingValues = paddingValues,
                allNotesState = allNotesState,
                scope = coroutineScope,
                scaffoldState = scaffoldState,
                onNoteClick = onNoteClick,
                onNoteSwipe = viewModel::onNoteSwipe,
                onSearchChange = viewModel::onSearchChange,
                clearSearchBar = viewModel::clearSearchBar
            )
        },
        floatingActionButton = {
            AllNotesFab(
                onNoteClick
            )
        }
    )
}
