package com.gmail.pentominto.us

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.Utility.NoRippleInteractionSource
import com.gmail.pentominto.us.supernotes.noteeditscreen.NoteEditScreenViewModel
import com.gmail.pentominto.us.supernotes.ui.theme.BrownBark
import com.gmail.pentominto.us.supernotes.ui.theme.LighterWalnutBrown
import com.gmail.pentominto.us.supernotes.ui.theme.Powder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteEditScreen(
    noteId : Long,
    viewModel : NoteEditScreenViewModel = hiltViewModel()
) {

    if (noteId != 0L) {
        viewModel.getNote(noteId)
    } else {
        viewModel.insertNewNote()
    }

    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifeCycleOwner) {

        val observer = LifecycleEventObserver { source, event ->

            when (event) {

                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> viewModel.updateNote()
                else                                              -> {
                    //Nothing
                }
            }

        }

        lifeCycleOwner.addObserver(observer)

        onDispose {
            lifeCycleOwner.removeObserver(observer)
        }
    }

    val noteState = remember { viewModel.noteState }

    val configuration = LocalConfiguration.current

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    var isExpanded = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .background(Powder)
            .fillMaxSize()
            .padding(16.dp),
        scaffoldState = scaffoldState,
        content =
        { padding ->

            Column(
                modifier = Modifier.padding(padding)
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    elevation = 1.dp
                ) {

                    Row(
                        modifier = Modifier
                            .background(LighterWalnutBrown)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = noteState.value.noteTitle,
                            singleLine = true,
                            placeholder = { Text(text = "Enter a Title...") },
                            onValueChange = { viewModel.onTitleInputChange(it) },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color.Transparent
                                ),
                            colors = TextFieldDefaults.textFieldColors(
                                disabledTextColor = Color.Transparent,
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )

                        Icon(
                            painterResource(id = R.drawable.ic_baseline_delete_24),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable(
                                    interactionSource = NoRippleInteractionSource(),
                                    onClick = {
                                    },
                                    indication = null
                                ),
                            contentDescription = null,
                        )

                        Icon(
                            painterResource(id = R.drawable.ic_baseline_more_vert_24),
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .clickable(
                                    interactionSource = NoRippleInteractionSource(),
                                    onClick = {},
                                    indication = null
                                ),
                            contentDescription = null,
                        )
                    }

                }

                Divider(
                    color = BrownBark
                )

                Card(
                    modifier = Modifier
                        .weight(1f),
                    elevation = 1.dp,
                    shape = RoundedCornerShape(4.dp),
                    backgroundColor = LighterWalnutBrown

                ) {

                    Row() {
                        TextField(
                            value = noteState.value.noteBody,
                            placeholder = { Text(text = "Enter Text...") },
                            onValueChange = { viewModel.onBodyInputChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.Transparent
                                ),
                            colors = TextFieldDefaults.textFieldColors(
                                disabledTextColor = Color.Transparent,
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }

                }

                Divider(
                    color = BrownBark
                )

                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            elevation = 0.dp,
                            shape = RoundedCornerShape(2.dp),
                            backgroundColor = LighterWalnutBrown
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Row(
                                    modifier = Modifier.heightIn()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .clip(CircleShape)
                                            .background(Color.LightGray)
                                            .clickable {

                                                scope.launch {

                                                    isExpanded.value = ! isExpanded.value
                                                    bottomDrawerState.open()

                                                }

                                            }
                                    ) {

                                        Text(
                                            text = "Other",
                                            modifier = Modifier.padding(
                                                top = 4.dp,
                                                bottom = 4.dp,
                                                start = 8.dp,
                                                end = 8.dp
                                            )
                                        )

                                    }
                                }


                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier
                                ) {

                                    Text(
                                        text = "Created : 4/13/22",
                                        fontStyle = FontStyle.Italic
                                    )
                                    Text(
                                        text = "Last Modified : 5/1/22",
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }

                            AnimatedVisibility(
                                visible = isExpanded.value,
                            ) {

                                BottomDrawer(
                                    modifier = Modifier
                                        .height(200.dp)
                                        .background(Color.Red)
                                    //maybe make a swipeable box instead of this bs
                                    ,
                                    drawerState = bottomDrawerState,
                                    content = {
                                        Text(text = "yo")
                                    },
                                    gesturesEnabled = true,
                                    drawerContent = {
                                        Text(text = "To")
                                    })

                            }

                        }
                    }
                    else                               -> {
                    }
                }
            }

        }
    )
}

//AnimatedVisibility(visible = isExpanded.value) {
//
//    BottomDrawer(
//        modifier = Modifier.height(200.dp),
//        drawerState = bottomDrawerState,
//        drawerContent = {
//            Text(text = "yo")
//        }) {
//    }
//}