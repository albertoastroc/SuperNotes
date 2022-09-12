package com.gmail.pentominto.us

import android.content.res.Configuration
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
import com.gmail.pentominto.us.supernotes.noteeditscreen.CategoriesList
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

    LaunchedEffect(
        key1 = noteId,
    ) {
        if (noteId != 0L) {
            viewModel.getNote(noteId)
        } else {
            viewModel.insertNewNote()
        }
    }

    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    val noteState by remember { viewModel.note }

    val categories by remember { viewModel.categories }

    val configuration = LocalConfiguration.current

    val scope = rememberCoroutineScope()

    val bottomDrawerState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomDrawerState)

    var dialogState by remember { mutableStateOf(false) }

    var checkboxState by remember { mutableStateOf(false) }

    DisposableEffect(lifeCycleOwner) {

        val observer = LifecycleEventObserver { _, event ->

            when (event) {

                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP,
                Lifecycle.Event.ON_DESTROY -> viewModel.updateNote()
                else                       -> {
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
        content = {



            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(4.dp),
                    elevation = 1.dp
                ) {

                    Row(
                        modifier = Modifier
                            .background(LighterWalnutBrown)
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        TextField(
                            value = noteState.noteTitle,
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

                    TextField(
                        value = noteState.noteBody,
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


                Divider(
                    color = BrownBark
                )
                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {

                        Card(
                            modifier = Modifier,
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

                                Button(
                                    onClick = {
                                        scope.launch {

                                            bottomDrawerState.expand()
                                        }
                                    },
                                    modifier = Modifier
                                        .clip(CircleShape),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
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
                    else                               -> {}
                }
            }

        },
        sheetContent = {

            CategoriesList(
                categories = categories
            )
        }
    )
}