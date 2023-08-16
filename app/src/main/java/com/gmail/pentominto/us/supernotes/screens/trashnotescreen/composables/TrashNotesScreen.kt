@file:OptIn(ExperimentalFoundationApi::class)

package com.gmail.pentominto.us.supernotes.screens.trashnotescreen.composables

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.composables.NavigationDrawerOptionsContainer
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.TrashNoteItem
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.TrashNotesViewModel
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TrashNotesScreen(
    viewModel : TrashNotesViewModel = hiltViewModel(),
    onTrashNoteClick : (Int) -> Unit,
    onDrawerItemClick : (Int) -> Unit
) {

    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = true,
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            NavigationDrawerOptionsContainer(
                drawerState = scaffoldState.drawerState,
                onDrawerItemClick = onDrawerItemClick
            )
        },
    ) {

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
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
                        tween(durationMillis = 400)
                    )
                )
            }
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
