@file:OptIn(ExperimentalMaterialApi::class)

package com.gmail.pentominto.us.supernotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.AllNotesViewModel
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.NoteItem
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.NoteItemSearchResult
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.SearchBarWithMenu
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.MenuItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
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
                        title = "Privacy policy and info",
                        icon = Icons.Default.Info
                    ),

                    MenuItem(
                        id = 4,
                        title = "Rate me in the Play Store!",
                        icon = Icons.Default.Star
                    ),
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

                        SwipingBackground(
                            deleteNote = { viewModel.deleteNote(note.noteId) },
                            noteId = note.noteId
                        ) {

                            NoteItemSearchResult(
                                note = note,
                                query = allNotesState.value.searchBarInput,
                                modifier = Modifier,
                                onClick = { onNoteClick(note.noteId) }
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

                            SwipingBackground(
                                deleteNote = { viewModel.deleteNote(note.noteId) },
                                noteId = note.noteId
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

                        SwipingBackground(
                            deleteNote = { viewModel.deleteNote(note.noteId) },
                            noteId = note.noteId
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

@Composable
fun SwipingBackground(
    deleteNote : (Long) -> Unit,
    noteId : Long,
    content : @Composable () -> Unit
) {

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) {
                deleteNote(noteId)
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        dismissThresholds = { FractionalThreshold(.6f) },
        background = {

            if (dismissState.progress.fraction <= .99f) {

                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.secondary
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_delete_32),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSecondary,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_32),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                }
            }
        },
        dismissContent = {
            content()
        }
    )
}

