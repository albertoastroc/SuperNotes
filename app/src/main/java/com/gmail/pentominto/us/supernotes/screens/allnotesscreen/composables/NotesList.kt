package com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.AllNotesState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesList(
    paddingValues: PaddingValues,
    allNotesState: AllNotesState,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    onNoteClick: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    clearSearchBar: () -> Unit,
    onNoteSwipe: (Note) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(1f),
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
                items = notes.filterNotes(category, allNotesState.searchBarInput ),
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

    return this.filter {  it.noteBody.lowercase().contains(searchBarInput) }
        .filter { category.categoryTitle != "TrashNotesAPPTAG" }

}