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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onNoteClick : (Long) -> Unit,
    onOptionsClick : (Int) -> Unit,
) {

    var state by remember { mutableStateOf(viewModel.searchBarText) }

    val scaffoldState = rememberScaffoldState()

    val showCategories = remember { viewModel.categoriesOptionsState }

    val notes by remember { viewModel.notesListWithCategories }

    val categories by remember { viewModel.categories }

    LaunchedEffect(
        key1 = viewModel.notesListWithCategories,
        key2 = viewModel.notesListNoCategories,
        block = {
            viewModel.getNotesWithCategories()
            viewModel.getNotesNoCategories()
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
                        id = 1,
                        title = "Home",
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = 2,
                        title = "Options",
                        icon = Icons.Default.Settings
                    ),
                    MenuItem(
                        id = 3,
                        title = "Backup settings",
                        icon = Icons.Default.Build
                    ),
                    MenuItem(
                        id = 4,
                        title = "Privacy policy and info",
                        icon = Icons.Default.Info
                    ),

                    MenuItem(
                        id = 5,
                        title = "Rate me in the Play Store!",
                        icon = Icons.Default.Star
                    ),

                    ),
                categoriesList = categories,
                onSettingClick = { onOptionsClick(it) },
                onCategoryClick = {}
            )
        },

        topBar = {
            SearchBarWithMenu(
                input = state.value,
                scaffoldState = scaffoldState,
                onInputChange = { viewModel.onSearchChange(state.value) }
            )

        },
        content = { paddingValues ->

            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {

                if (!showCategories.value) {

                    items(
                        items = viewModel.notesListNoCategories.value,
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
                                onClick = { onNoteClick(note.noteId) }
                            )
                        }
                    }
                } else {

                    notes.entries.forEach { (category, notes) ->

                        item {
                            Text(
                                text = category.categoryTitle.toString(),
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 24.sp
                            )
                        }

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
                                    onClick = { onNoteClick(note.noteId) }
                                )
                            }
                        }
                    }
                }
            }

        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = { Text(text = "New Note") },
                onClick = {
                    onNoteClick(0L)
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

