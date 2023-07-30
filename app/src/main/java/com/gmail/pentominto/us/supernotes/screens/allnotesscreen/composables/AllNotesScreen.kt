package com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.AllNotesViewModel

@Composable
fun AllNotesScreen(
    viewModel: AllNotesViewModel = hiltViewModel(),
    onNoteClick: (Int) -> Unit,
    onDrawerItemClick: (Int) -> Unit
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
                scope = coroutineScope,
                scaffoldState = scaffoldState,
                onDrawerItemClick = onDrawerItemClick
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
                onNoteClick = onNoteClick
            )
        }
    )
}
