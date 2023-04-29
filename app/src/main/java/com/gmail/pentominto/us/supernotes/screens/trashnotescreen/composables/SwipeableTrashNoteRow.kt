@file:OptIn(ExperimentalMaterialApi::class)

package com.gmail.pentominto.us.supernotes.screens.trashnotescreen.composables

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
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.utility.Constants.CURRENT_POSITION
import com.gmail.pentominto.us.supernotes.utility.Constants.FRACTION_THRESHOLD

@Composable
fun SwipeableTrashNoteRow(
    deleteNote: (Int) -> Unit,
    restoreNote: () -> Unit,
    trashNote: Note,
    modifier : Modifier,
    content: @Composable () -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) {
                deleteNote(trashNote.noteId)
            } else if (it == DismissValue.DismissedToStart) {
                restoreNote()
            }

            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        directions = setOf(
            DismissDirection.StartToEnd,
            DismissDirection.EndToStart
        ),
        dismissThresholds = { FractionalThreshold(FRACTION_THRESHOLD) },
        background = {
            val direction = dismissState.dismissDirection?.name

            if (
                dismissState.progress.fraction <= CURRENT_POSITION && direction == DismissDirection.StartToEnd.name
            ) {
                Box(
                    modifier = Modifier.background(
                        MaterialTheme.colors.secondary
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
            } else if (
                dismissState.progress.fraction <= CURRENT_POSITION && direction == DismissDirection.EndToStart.name
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.secondary
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_32),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSecondary
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_restore_32),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSecondary,
                            modifier = Modifier.padding(horizontal = 24.dp)
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
