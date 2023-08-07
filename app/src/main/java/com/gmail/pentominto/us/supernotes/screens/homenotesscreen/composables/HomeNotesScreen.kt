package com.gmail.pentominto.us.supernotes.screens.homenotesscreen.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.composables.NavigationDrawerOptions
import com.gmail.pentominto.us.supernotes.screens.homenotesscreen.HomeScreenNotesViewModel

@Composable
fun HomeNotesScreen(
    viewModel: HomeScreenNotesViewModel = hiltViewModel(),
    onNoteClick: (Int) -> Unit,
    onDrawerItemClick: (Int) -> Unit
) {
    val homeNotesState by remember { viewModel.homeScreenNotesState }

    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = true,
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            NavigationDrawerOptions(
                drawerState = scaffoldState.drawerState,
                onDrawerItemClick = onDrawerItemClick
            )
        },
        content = { paddingValues ->

            NotesList(
                paddingValues = paddingValues,
                homeScreenNotesState = homeNotesState,
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
