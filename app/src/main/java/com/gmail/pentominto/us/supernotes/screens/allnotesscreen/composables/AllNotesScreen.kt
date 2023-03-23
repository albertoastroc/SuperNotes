package com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.Drawer
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AllNotesScreen(
    viewModel : AllNotesViewModel = hiltViewModel(),
    onNoteClick : (Int) -> Unit,
    onOptionsClick : (Int) -> Unit
) {
    val allNotesState by remember { viewModel.allNotesState }

    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()

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
                listState = lazyListState,
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
            ExtendedFab(
                lazyListState,
                onNoteClick
            )
        }
    )
}

@Composable
private fun NotesList(
    paddingValues : PaddingValues,
    listState : LazyListState,
    allNotesState : AllNotesState,
    scope : CoroutineScope,
    scaffoldState : ScaffoldState,
    onNoteClick : (Int) -> Unit,
    onSearchChange : (String) -> Unit,
    clearSearchBar : () -> Unit,
    onNoteSwipe : (Note) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(1f),
        state = listState,
        contentPadding = paddingValues
    ) {
        item {
            SearchBarWithMenu(
                input = allNotesState.searchBarInput,
                onInputChange = { onSearchChange(it) },
                onXClick = { clearSearchBar() },
                onMenuIconClick = { scope.launch { scaffoldState.drawerState.open() } }
            )
        }
        allNotesState.notes.entries.forEach { (category, notes) ->

            if (allNotesState.showCategoryTitles && allNotesState.searchBarInput.isEmpty()) {
                item { CategoryTitle(category) }
            }

            items(
                items = notes.filter
                { it.noteBody.contains(allNotesState.searchBarInput) }.filter
                { it.category != "TrashNotesAPPTAG" },
                key = { it.noteId }
            ) { note ->

                DefaultNote(
                    note,
                    onNoteClick,
                    onNoteSwipe
                )
            }
        }
    }
}

@Composable
private fun CategoryTitle(category : Category) {
    Text(
        text = category.categoryTitle,
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        color = MaterialTheme.colors.onBackground,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 22.sp
    )
}

@Composable
private fun DefaultNote(
    note : Note,
    onNoteClick : (Int) -> Unit,
    onNoteSwipe : (Note) -> Unit
) {
    SwipeableNoteRow(
        deleteNote = { onNoteSwipe(note) }

    ) {
        NoteItem(
            note = note,
            modifier = Modifier,
            onClick = { onNoteClick(note.noteId) }
        )
    }
}

@Composable
private fun AllNotesDrawer(
    scope : CoroutineScope,
    scaffoldState : ScaffoldState,
    onOptionsClick : (Int) -> Unit
) {
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
                title = "Rate me in the Play Store!",
                icon = Icons.Default.Star
            ),
            MenuItem(
                id = 5,
                title = "Privacy policy and info",
                icon = Icons.Default.Info
            )
        ),
        onSettingClick = {
            scope.launch {
                scaffoldState.drawerState.close()
                onOptionsClick(it)
            }
        }
    )
}

@Composable
private fun ExtendedFab(listState : LazyListState, onNoteClick : (Int) -> Unit) {
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
        onClick = { onNoteClick(0) },
        containerColor = MaterialTheme.colors.secondary
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
