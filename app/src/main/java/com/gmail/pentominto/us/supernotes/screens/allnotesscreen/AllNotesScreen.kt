@file:OptIn(ExperimentalMaterialApi::class)

package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.Drawer
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.MenuItem
import kotlinx.coroutines.launch

@Composable
fun AllNotesScreen(
    viewModel : AllNotesViewModel = hiltViewModel(),
    onNoteClick : (Long) -> Unit,
    onOptionsClick : (Int) -> Unit,
) {

    val allNotesState by remember { mutableStateOf(viewModel.allNotesState) }

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    LaunchedEffect(
        key1 = allNotesState.value.notesWithCategory.size,
        block = {
            viewModel.getNotesList()
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerBackgroundColor = MaterialTheme.colors.background,
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
                        title = "Trash",
                        icon = Icons.Default.Delete
                    ),
                    MenuItem(
                        id = 4,
                        title = "Suggest a feature",
                        icon = Icons.Default.Email
                    ),
                    MenuItem(
                        id = 5,
                        title = "Rate me in the Play Store!",
                        icon = Icons.Default.Star
                    ),
                    MenuItem(
                        id = 6,
                        title = "Privacy policy and info",
                        icon = Icons.Default.Info
                    )
                ),
                onSettingClick = {

                    scope.launch {

                        scaffoldState.drawerState.close()
                        onOptionsClick(it)
                    }

                },
            )
        },
        content = { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(1f),
                state = listState,
                contentPadding = paddingValues
            ) {

                item {
                    SearchBarWithMenu(
                        input = allNotesState.value.searchBarInput,
                        onInputChange = { viewModel.onSearchChange(it) },
                        onXClick = { viewModel.clearSearchBar() },
                        onMenuIconClick = {

                            scope.launch {

                                scaffoldState.drawerState.open()
                            }
                        }
                    )
                }

                if (allNotesState.value.searchBarInput.length >= 3) {

                    items(allNotesState.value.notesSearchResults) { note ->

                        SwipeableNoteRow(
                            deleteNote = { viewModel.deleteNote(note.noteId) },
                            note = note,
                            trashEnabled = allNotesState.value.trashEnabled,
                            sendToTrash = { viewModel.sendToTrash(note) }
                        ) {

                            NoteItemSearchResult(
                                note = note,
                                query = allNotesState.value.searchBarInput,
                                modifier = Modifier,
                                onClick = { onNoteClick(note.noteId) },
                            )
                        }
                    }
                } else if (allNotesState.value.showCategories) {

                    allNotesState.value.notesWithCategory.entries.forEach { (category, notes) ->

                        item {
                            Text(
                                text = category.categoryTitle,
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 22.sp
                            )
                        }

                        items(
                            items = notes,
                            key = { it.noteId }
                        ) { note ->

                            SwipeableNoteRow(
                                deleteNote = { viewModel.deleteNote(note.noteId) },
                                note = note,
                                trashEnabled = allNotesState.value.trashEnabled,
                                sendToTrash = { viewModel.sendToTrash(note) }
                            ) {
                                NoteItem(
                                    note = note,
                                    modifier = Modifier,
                                    onClick = { onNoteClick(note.noteId) }
                                )
                            }
                        }
                    }
                } else {

                    items(
                        items = allNotesState.value.notesWithNoCategories,
                        key = { it.noteId }
                    ) { note ->

                        SwipeableNoteRow(
                            deleteNote = { viewModel.deleteNote(note.noteId) },
                            note = note,
                            trashEnabled = allNotesState.value.trashEnabled,
                            sendToTrash = { viewModel.sendToTrash(note) }
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
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = "New Note",
                        color = MaterialTheme.colors.onSecondary
                    )
                },
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSecondary
                    )
                },
                expanded = listState.isScrollingUp(),
                onClick = { onNoteClick(0L) },
                containerColor = MaterialTheme.colors.secondary,
            )
        }
    )
}

@Composable
fun LazyListState.isScrollingUp() : Boolean {

    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

