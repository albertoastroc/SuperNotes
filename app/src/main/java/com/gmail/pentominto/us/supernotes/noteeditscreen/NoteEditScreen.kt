package com.gmail.pentominto.us

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.gmail.pentominto.us.supernotes.ui.theme.LimishGreen
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

    val categories = mutableListOf(
        "movies",
        "music",
        "shopping food",
        "passwords",
        "classes",
        "movies",
        "music",
        "shopping food",
        "passwords",
        "classes",
    )

    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    val noteState = remember { viewModel.noteState }

    val configuration = LocalConfiguration.current

    val scope = rememberCoroutineScope()

    val bottomDrawerState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomDrawerState)

    val dialogState = remember { mutableStateOf(false) }

    val dialogText = remember { mutableStateOf("") }

    val checkboxState = remember { mutableStateOf(false) }

    DisposableEffect(lifeCycleOwner) {

        val observer = LifecycleEventObserver { _, event ->

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

    BottomSheetScaffold(
        modifier = Modifier
            .background(Powder)
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
        scaffoldState = scaffoldState,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        content = { padding ->

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
                                            .padding(start = 4.dp)
                                            .clip(CircleShape)
                                            .background(Color.LightGray)
                                            .clickable {

                                                scope.launch {

                                                    bottomDrawerState.expand()
                                                }
                                            }
                                    ) {

                                        Text(
                                            text = "Other",
                                            modifier = Modifier.padding(
                                                top = 8.dp,
                                                bottom = 8.dp,
                                                start = 12.dp,
                                                end = 12.dp
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
                        }
                    }
                    else                               -> {
                    }
                }
            }

        },
        sheetContent = {

            Column(
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Add to...",
                        modifier = Modifier.padding(8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                dialogState.value = true
                            }

                    ) {

                        if (dialogState.value) {

                            CategoryAlertDialog(dialogState = dialogState)
                        }

                        Row(
                            modifier = Modifier
                                .background(LimishGreen)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Add new category",
                                modifier = Modifier.padding(end = 8.dp, start = 8.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                                contentDescription = null,
                            )
                        }
                    }
                }

                Divider(modifier = Modifier
                    .height(1.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    items(categories) { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item,
                                modifier = Modifier.padding(top = 8.dp, start = 16.dp, bottom = 8.dp)
                            )

                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

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

                                Checkbox(
                                    checked = checkboxState.value,
                                    modifier = Modifier.padding(end = 8.dp),
                                    onCheckedChange = {
                                        checkboxState.value = it
                                    }
                                )
                            }


                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryAlertDialog(
    dialogState : MutableState<Boolean>
) {

    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = {
                dialogState.value = false
            },
            title = {
                Text(text = "New Category")
            },
            text = {
            },
            buttons = {
                Text(text = "A button")
            }
        )
    }
}