package com.gmail.pentominto.us.supernotes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.ui.theme.LighterWalnutBrown
import com.gmail.pentominto.us.supernotes.ui.theme.LimishGreen
import com.gmail.pentominto.us.supernotes.ui.theme.Pine
import com.gmail.pentominto.us.supernotes.ui.theme.Powder
import kotlinx.coroutines.launch

@Composable
fun AllNotesScreen() {

    val searchBarBackGroundColor = LimishGreen

    val notesList = listOf(
        Note(
            "shopping list",
            "Other"
        ),
        Note(
            "boots",
            "Other"
        ),
        Note(
            "math notes",
            "School"
        ),
        Note(
            "science trip",
            "School"
        ),
        Note(
            "movies",
            "Fun"
        ),
        Note(
            "shows",
            "Fun"
        ),
        Note(
            "games",
            "Fun"
        ),
        Note(
            "quotes",
            "Thinking"
        ),
        Note(
            "no cat",
            "Other"
        )
    )

    val groupedNotes = notesList.groupBy { it.category }.toSortedMap()

    val categoriesList = listOf(
        "Work",
        "School",
        "Shows",
        "Music",
        "YT",
        "Schedule",
        "Ice Cream",
        "Cheese-steak places"
    )

    var state = remember { mutableStateOf("") }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var menuExpanded = remember { mutableStateOf(false) }

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
                categoriesList = categoriesList,
                onSettingClick = {},
                onCategoryClick = {}
            )
        },

        topBar = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                    .clip(CircleShape)
                    .background(searchBarBackGroundColor),

                ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically
                ) {

                    Icon(
                        painterResource(id = R.drawable.ic_baseline_menu_24),
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clickable(
                                interactionSource = NoRippleInteractionSource(),
                                onClick = {

                                    scope.launch {

                                        scaffoldState.drawerState.open()
                                    }

                                },
                                indication = null
                            ),
                        contentDescription = null,
                    )


                    TextField(
                        modifier = Modifier.weight(1f),
                        value = state.value,
                        colors = TextFieldDefaults.textFieldColors(
                            disabledTextColor = Color.Transparent,
                            backgroundColor = searchBarBackGroundColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Search Notes..."
                            )
                        },
                        onValueChange = { state.value = it },
                    )

                    Column() {

                        Icon(
                            painterResource(id = R.drawable.ic_baseline_more_vert_24),
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .clickable(
                                    interactionSource = NoRippleInteractionSource(),
                                    onClick = {
                                        menuExpanded.value = ! menuExpanded.value
                                    },
                                    indication = null
                                ),
                            contentDescription = null,
                        )

                        DropdownMenu(
                            expanded = menuExpanded.value,
                            offset = DpOffset(x =  0.dp, y = 8.dp),
                            onDismissRequest = { menuExpanded.value = false }) {

                            DropdownMenuItem(onClick = { /*TODO*/ }) {
                                Text(text = "Item 1")
                            }

                            DropdownMenuItem(onClick = { /*TODO*/ }) {
                                Text(text = "Item 2")
                            }
                            DropdownMenuItem(onClick = { /*TODO*/ }) {
                                Text(text = "Item 3")
                            }
                        }

                    }


                }
            }
        },
        content = { padding ->

            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {

                groupedNotes.forEach { (notesCat, noteVals) ->

                    item {
                        CategoryHeader(categoryHeader = notesCat)
                    }

                    items(noteVals) { noteVal ->

                        NoteItem(noteTitle = noteVal.title)

                    }

                }

            }

        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = { Text(text = "NEW NOTE") },
                onClick = { /*TODO*/ },
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
fun CategoryHeader(
    categoryHeader : String
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = categoryHeader,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    bottom = 8.dp,
                    top = 8.dp
                )
                .align(CenterVertically),
            fontSize = 20.sp
        )

    }
}

@Composable
fun NoteItem(
    noteTitle : String
) {

    Card(
        modifier = Modifier
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

