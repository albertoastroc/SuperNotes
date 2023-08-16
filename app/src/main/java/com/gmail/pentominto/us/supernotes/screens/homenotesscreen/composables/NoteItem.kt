package com.gmail.pentominto.us.supernotes.screens.homenotesscreen.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gmail.pentominto.us.supernotes.data.Note

@Composable
fun NoteItem(
    note : Note,
    onNoteClick : (Int) -> Unit,
    onNoteSwipe : (Note) -> Unit,
    modifier : Modifier
) {
    SwipeContainer(
        modifier = modifier,
        deleteNote = { onNoteSwipe(note) }

    ) {
        NoteContentContainer(
            note = note,
            modifier = modifier,
            onClick = { onNoteClick(it) }
        )
    }
}
