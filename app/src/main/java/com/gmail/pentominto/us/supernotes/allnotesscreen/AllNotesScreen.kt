package com.gmail.pentominto.us.supernotes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.allnotesscreen.AllNotesViewModel
import com.gmail.pentominto.us.supernotes.allnotesscreen.SearchBarWithMenu
import com.gmail.pentominto.us.supernotes.ui.theme.LighterWalnutBrown
import com.gmail.pentominto.us.supernotes.ui.theme.Pine
import com.gmail.pentominto.us.supernotes.ui.theme.Powder

@Composable
fun AllNotesScreen(
    viewModel : AllNotesViewModel = hiltViewModel(),
    onClick : (Long) -> Unit,

    ) {

    var state = remember { mutableStateOf("") }

    val scaffoldState = rememberScaffoldState()

    val notesState by remember { viewModel.notesList }

    val categories by remember { viewModel.categories }

    LaunchedEffect(
        key1 = viewModel.notesList,
        block = {
            viewModel.getNotes()
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = Powder,
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {

            Drawer(
                drawerOptionsList = listOf(
                    MenuItem(
                        id = "1",
                        title = "Home",
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = "2",
                        title = "Settings",
                        icon = Icons.Default.Settings
                    ),
                    MenuItem(
                        id = "3",
                        title = "Backup settings",
                        icon = Icons.Default.Build
                    ),
                    MenuItem(
                        id = "3",
                        title = "Privacy policy and info",
                        icon = Icons.Default.Info
                    ),

                    MenuItem(
                        id = "3",
                        title = "Rate me in the Play Store!",
                        icon = Icons.Default.Star
                    ),

                    ),
                categoriesList = categories,
                onSettingClick = {},
                onCategoryClick = {}
            )
        },

        topBar = {
            SearchBarWithMenu(
                state = state,
                scaffoldState = scaffoldState
            )

        },
        content = { paddingValues ->

//            LazyColumn(
//                modifier = Modifier.padding(padding)
//            ) {
//
//                groupedNotes.forEach { (notesCat, noteVals) ->
//
//                    item {
//                        CategoryHeader(categoryHeader = notesCat)
//                    }
//
//                    items(noteVals) { noteVal ->
//
//                        NoteItem(noteTitle = noteVal.title)
//
//                    }
//
//                }
//
//            }

            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(
                    items = notesState,
                    key = { it.noteId }
                ) { note ->

                    NoteItem(
                        noteTitle = note.noteTitle,
                        modifier = Modifier.clickable {
                            onClick(note.noteId)
                        }
                    )
                }
            }
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = { Text(text = "NEW NOTE") },
                onClick = {
                    onClick(0L)
                },
                backgroundColor = Pine,
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                    )
                },
                contentColor = Color.White
            )

        }
    )
}

@Composable
fun NoteItem(
    noteTitle : String,
    modifier : Modifier
) {

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp,
                top = 8.dp
            ),
        elevation = 1.dp,
        shape = RoundedCornerShape(4.dp),
        backgroundColor = LighterWalnutBrown

    ) {

        Text(
            text = noteTitle,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .background(
                    color = Color.Transparent
                ),
            fontSize = 20.sp
        )
    }
}

