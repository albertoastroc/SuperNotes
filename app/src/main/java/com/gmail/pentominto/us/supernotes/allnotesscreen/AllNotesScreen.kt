package com.gmail.pentominto.us.supernotes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.allnotesscreen.AllNotesViewModel
import com.gmail.pentominto.us.supernotes.allnotesscreen.NoteItem
import com.gmail.pentominto.us.supernotes.allnotesscreen.NoteItemSearchResult
import com.gmail.pentominto.us.supernotes.allnotesscreen.SearchBarWithMenu
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllNotesScreen(
    viewModel : AllNotesViewModel = hiltViewModel(),
    onNoteClick : (Long) -> Unit,
    onOptionsClick : (Int) -> Unit,
) {

    val searchState by remember { mutableStateOf(viewModel.searchBarText) }

    val scaffoldState = rememberScaffoldState()

    val showCategories = remember { viewModel.categoriesOptionsState }

    val notesWithCategories by remember { viewModel.notesListWithCategories }

    val notesWithNoCategories by remember { viewModel.notesListNoCategories }

    val notesSearchResult by remember { viewModel.notesListSearchResults }

    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

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
                onSettingClick = {

                    scope.launch {

                        scaffoldState.drawerState.close()
                        onOptionsClick(it)
                    }

                },
            )
        },

        topBar = {
            SearchBarWithMenu(
                input = searchState.value,
                onInputChange = { viewModel.onSearchChange(it) },
                onMenuIconClick = {

                    scope.launch {

                        scaffoldState.drawerState.open()
                    }
                }
            )

        },
        content = { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(paddingValues),
                state = listState
            ) {

                if (searchState.value.length >= 3) {

                    items(notesSearchResult) { note ->

                        NoteItemSearchResult(
                            note = note,
                            query = searchState.value,
                            modifier = Modifier,
                            onClick = { onNoteClick(note.noteId) }
                        )

                    }
                } else if (showCategories.value) {

                    notesWithCategories.entries.forEach { (category, notes) ->

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
                } else {

                    items(
                        items = notesWithNoCategories,
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
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = { Text(
                    text = "New Note",
                    color = MaterialTheme.colors.onSecondary
                ) },
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
fun ListWithCategories() {
}

@Composable
fun ListWithNoCategories() {
}

@Composable
fun ListSearchResults() {
}

