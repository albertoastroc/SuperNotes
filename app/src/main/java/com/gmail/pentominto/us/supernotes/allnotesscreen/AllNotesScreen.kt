package com.gmail.pentominto.us.supernotes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

    LaunchedEffect(
        key1 = viewModel.notesListWithCategories,
        key2 = viewModel.notesListNoCategories,
        block = {
            viewModel.getNotesWithCategories()
            viewModel.getNotesNoCategories()
        }
    )

    val verticalScroll = rememberScrollState()
    var fabExtended by remember { mutableStateOf(true) }

    LaunchedEffect(verticalScroll) {
        var prev = 0
        snapshotFlow { verticalScroll.value }
            .collect {
                fabExtended = it <= prev
                prev = it
            }
    }

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
                    .padding(paddingValues)
                    .verticalScroll(verticalScroll)
                ,
            ) {

                if (searchState.value.length >= 3) {

                    items(notesSearchResult,) { note ->

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

            ExtendableFloatingActionButton(
                extended = fabExtended,
                text = { Text(text = "New Note") },
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                    )
                }

            )

//            ExtendedFloatingActionButton(
//                text = {
//                    Text(
//                        text = "New Note",
//                        fontSize = 16.sp
//                    )
//                },
//                onClick = {
//                    onNoteClick(0L)
//                },
//                backgroundColor = MaterialTheme.colors.secondary,
//                icon = {
//                    Icon(
//                        painterResource(id = R.drawable.ic_baseline_add_24),
//                        contentDescription = null,
//                    )
//                },
//            )
        }
    )
}

@Composable
fun ExtendableFloatingActionButton(
    modifier: Modifier = Modifier,
    extended: Boolean,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit = {}
) {

    val paddingSize = 16.dp

    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(
                start = paddingSize,
                end = paddingSize
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            AnimatedVisibility(visible = extended) {
                Row {
                    Spacer(Modifier.width(12.dp))
                    text()
                }
            }
        }
    }
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

