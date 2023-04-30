@file:OptIn(ExperimentalFoundationApi::class)

package com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.AllNotesState
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.AllNotesViewModel
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
            CustomFab(
                onNoteClick
            )
        }
    )
}

@Composable
fun NotesList(
    paddingValues : PaddingValues,
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
        contentPadding = paddingValues,

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
                item(key = category.categoryId) {
                    CategoryTitle(
                        category,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(durationMillis = 400)
                        )
                    )
                }
            }

            items(
                items = notes.filter
                { it.noteBody.contains(allNotesState.searchBarInput) }.filter
                { it.category != "TrashNotesAPPTAG" },
                key = { it.noteId }
            ) { note ->

                DefaultNote(
                    note = note,
                    onNoteClick = onNoteClick,
                    onNoteSwipe = onNoteSwipe,
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = tween(durationMillis = 400)
                    )
                )
            }
        }
    }
}

@Composable
private fun CategoryTitle(
    category : Category,
    modifier : Modifier
) {
    Text(
        text = category.categoryTitle,
        modifier = modifier
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
    onNoteSwipe : (Note) -> Unit,
    modifier : Modifier
) {
    SwipeNoteRowContainer(
        modifier = modifier,
        deleteNote = { onNoteSwipe(note) }

    ) {
        NoteItem(
            note = note,
            modifier = modifier,
            onClick = { onNoteClick(it) }
        )
    }
}

@Composable
private fun CustomFab(onNoteClick : (Int) -> Unit) {
    FloatingActionButton(
        content = {
            Icon(
                painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = null,
                tint = MaterialTheme.colors.onSecondary
            )
        },
        onClick = { onNoteClick(0) },
        backgroundColor = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(12.dp)
    )
}
