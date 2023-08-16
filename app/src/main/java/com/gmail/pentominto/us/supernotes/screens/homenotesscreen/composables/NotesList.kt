package com.gmail.pentominto.us.supernotes.screens.homenotesscreen.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.screens.homenotesscreen.HomeScreenNotesState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesList(
    homeScreenNotesState : HomeScreenNotesState,
    scaffoldState : ScaffoldState,
    onNoteClick : (Int) -> Unit,
    onSearchChange : (String) -> Unit,
    clearSearchBar : () -> Unit,
    onNoteSwipe : (Note) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp)

    ) {
        item {
            SearchBarWithMenu(
                input = homeScreenNotesState.searchBarInput,
                onInputChange = { onSearchChange(it) },
                onXClick = { clearSearchBar() },
                onMenuIconClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }
            )
        }
        homeScreenNotesState.notes.entries.forEach { (category, notes) ->

            if (homeScreenNotesState.showCategories && homeScreenNotesState.searchBarInput.isEmpty()) {
                item(key = category.categoryTitle) {
                    CategoryHeader(
                        category,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(durationMillis = 400)
                        )
                    )
                }
            }

            items(
                items = notes.filterNotes(category,
                    homeScreenNotesState.searchBarInput
                ),
                key = { it.noteId }
            ) { note ->

                NoteItem(
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

fun List<Note>.filterNotes(category : Category, searchBarInput : String) : List<Note> {
    return this.filter { it.noteBody.lowercase().contains(searchBarInput) }
        .filter { category.categoryTitle != "TrashNotesAPPTAG" }
}
