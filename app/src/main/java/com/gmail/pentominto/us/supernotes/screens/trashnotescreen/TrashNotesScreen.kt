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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.SwipeableTrashNoteRow
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.TrashNoteItem
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.TrashNotesViewModel

@Composable
fun TrashNotesScreen(
    viewModel : TrashNotesViewModel = hiltViewModel(),
    onTrashNoteClick : () -> Unit
) {

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

        items(viewModel.trashNotesList.value) { note ->

            SwipeableTrashNoteRow(
                deleteNote = {},
                restoreNote = {},
                trashNote = note
            ) {

                TrashNoteItem(
                    note = note,
                    modifier = Modifier,
                    onClick = {
                        onTrashNoteClick()
                    }
                )
            }
        }
    }
}