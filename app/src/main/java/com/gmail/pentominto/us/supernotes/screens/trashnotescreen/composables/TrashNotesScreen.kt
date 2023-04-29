@file:OptIn(ExperimentalFoundationApi::class)

package com.gmail.pentominto.us.supernotes.screens.trashnotescreen.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.TrashNoteItem
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.TrashNotesViewModel

@Composable
fun TrashNotesScreen(
    viewModel: TrashNotesViewModel = hiltViewModel(),
    onTrashNoteClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Trash",
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }
        }

        items(
            items = viewModel.trashNotesList.value,
            key = { it.noteId }
        ) { note ->

            TrashNote(
                viewModel,
                note,
                onTrashNoteClick,
                modifier = Modifier.animateItemPlacement(
                    tween(durationMillis = 600)
                )
            )
        }
    }
}

@Composable
private fun TrashNote(
    viewModel : TrashNotesViewModel,
    note : Note,
    onTrashNoteClick : (Int) -> Unit,
    modifier : Modifier
) {
    SwipeableTrashNoteRow(
        modifier = modifier,
        deleteNote = {
            viewModel.deleteTrashNote(note.noteId)
        },
        restoreNote = {
            viewModel.restoreTrashNote(note)
        },
        trashNote = note
    ) {
        TrashNoteItem(
            note = note,
            modifier = modifier,
            onClick = {
                onTrashNoteClick(note.noteId)
            }
        )
    }
}
