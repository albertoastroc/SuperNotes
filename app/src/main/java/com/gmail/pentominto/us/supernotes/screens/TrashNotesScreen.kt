package com.gmail.pentominto.us.supernotes.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TrashNotesScreen(

) {

    val trashNotes = listOf("note1", "note2", "note3", "and so on")

    LazyColumn(){

        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Trash")
            }
        }

        items(trashNotes) { note ->

            Row() {

                Text(text = note)
            }

        }

    }

}