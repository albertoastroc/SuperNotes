package com.gmail.pentominto.us.supernotes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.allnotesscreen.AllNotesViewModel
import com.gmail.pentominto.us.supernotes.allnotesscreen.NoteItem
import com.gmail.pentominto.us.supernotes.allnotesscreen.SearchBarWithMenu
import com.gmail.pentominto.us.supernotes.ui.theme.Pine
import com.gmail.pentominto.us.supernotes.ui.theme.Powder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllNotesScreen(
    viewModel : AllNotesViewModel = hiltViewModel(),
    onClick : (Long) -> Unit,
    ) {

    var state by remember { mutableStateOf(viewModel.searchBarText) }

    val scaffoldState = rememberScaffoldState()

    val notes by remember { viewModel.notesList }

    val categories by remember { viewModel.categories }
    
    LaunchedEffect(
        key1 = viewModel.notesList,
        block = {
            viewModel.getNotes()
            viewModel.testGetAllNotes()
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = Powder,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {

            Drawer(
                drawerOptionsList = listOf(
                    MenuItem(
                        id = "1",
                        title = "Home",
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = "2",
                        title = "Settings",
                        icon = Icons.Default.Settings
                    ),
                    MenuItem(
                        id = "3",
                        title = "Backup settings",
                        icon = Icons.Default.Build
                    ),
                    MenuItem(
                        id = "3",
                        title = "Privacy policy and info",
                        icon = Icons.Default.Info
                    ),

                    MenuItem(
                        id = "3",
                        title = "Rate me in the Play Store!",
                        icon = Icons.Default.Star
                    ),

                    ),
                categoriesList = categories,
                onSettingClick = {},
                onCategoryClick = {}
            )
        },

        topBar = {
            SearchBarWithMenu(
                input = state.value,
                scaffoldState = scaffoldState,
                onInputChange = {viewModel.onSearchChange(state.value)}
            )

        },
        content = { paddingValues ->

//            LazyColumn(
//                modifier = Modifier.padding(padding)
//            ) {
//
//                groupedNotes.forEach { (notesCat, noteVals) ->
//
//                    item {
//                        CategoryHeader(categoryHeader = notesCat)
//                    }
//
//                    items(noteVals) { noteVal ->
//
//                        NoteItem(noteTitle = noteVal.title)
//
//                    }
//
//                }
//
//            }

            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(
                    items = notes,
                    key = { it.noteId }
                ) { note ->

                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd) {
                                viewModel.deleteNote(note.noteId)
                            }
                            true
                        }
                    )
                    
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.StartToEnd),
                        dismissThresholds = { FractionalThreshold(.6f) },
                        background = {},
                    ) {

                        NoteItem(
                            note = note,
                            modifier = Modifier,
                            onClick = onClick
                        )

                    }


                }
            }
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = { Text(text = "New Note") },
                onClick = {
                    onClick(0L)
                },
                backgroundColor = Pine,
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                    )
                },
                contentColor = Color.White
            )

        }
    )
}

