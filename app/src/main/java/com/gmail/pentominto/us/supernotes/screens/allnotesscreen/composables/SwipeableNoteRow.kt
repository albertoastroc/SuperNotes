@file:OptIn(ExperimentalMaterialApi::class)

package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.data.SavedNote
import com.gmail.pentominto.us.supernotes.ui.theme.Scarlet

@Composable
fun SwipeableNoteRow(
    deleteNote : (Int) -> Unit,
    sendToTrash : () -> Unit,
    note : SavedNote,
    trashEnabled : Boolean,
    content : @Composable () -> Unit,
) {

    val dismissState = rememberDismissState(
        confirmStateChange = {

            if (it == DismissValue.DismissedToEnd) {

                if (trashEnabled) {
                    sendToTrash()
                    deleteNote(note.noteId)
                } else {

                    deleteNote(note.noteId)
                }
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        dismissThresholds = { FractionalThreshold(.6f) },
        background = {
            if (
                dismissState.progress.fraction <= .99f

            ) {

                Box(
                    modifier = Modifier
                        .background(
                            Scarlet
//                            MaterialTheme.colors.secondary
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_delete_32),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSecondary,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_32),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                }
            }
        },
        dismissContent = {
            content()
        }
    )
}