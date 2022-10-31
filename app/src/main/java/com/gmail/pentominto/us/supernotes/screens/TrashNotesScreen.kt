package com.gmail.pentominto.us.supernotes.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.NoteItem

@Preview
@Composable
fun TrashNotesScreen(

) {

    val trashNotes = listOf("note1", "note2", "note3", "and so on")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ){

        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Trash",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }
        }

        items(trashNotes) { note ->

            NoteItem(
                note = Note(note),
                modifier = Modifier,
                onClick = {}
            )

        }

    }

}