package com.gmail.pentominto.us

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.gmail.pentominto.us.supernotes.Utility.LogCompositions
import com.gmail.pentominto.us.supernotes.Utility.NoRippleInteractionSource
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.noteeditscreen.NoteEditScreenViewModel
import com.gmail.pentominto.us.supernotes.ui.theme.*
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
                Lifecycle.Event.ON_DESTROY -> viewModel.updateNoteText()
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
                            value = noteState.noteTitle.toString(),
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
                        value = noteState.noteBody.toString(),
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
                categories = categories,
                onClick = { viewModel.insertCategory(it) }
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesList(
    categories : List<Category>,
    onClick : (String) -> Unit
) {

    val openDialog = remember { mutableStateOf(false) }

    val dialogInput = remember { mutableStateOf("") }

    val dialogTitleState = remember { mutableStateOf("") }

    LogCompositions(
        tag = "TAG",
        msg = "CategoriesList"
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 450.dp)
    ) {

        stickyHeader() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrownBark)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            openDialog.value = true
                        }

                ) {

                    Row(
                        modifier = Modifier
                            .background(LimishGreen)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Add new category",
                            modifier = Modifier.padding(
                                end = 8.dp,
                                start = 8.dp
                            )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_add_24),
                            contentDescription = null,
                        )
                    }
                }

                if (openDialog.value) {

                    AlertDialog(
                        onDismissRequest = { openDialog.value = false },
                        title = {
                            Text(
                                text = dialogTitleState.value
                            )
                        },
                        text = {
                            TextField(
                                value = dialogInput.value,
                                modifier = Modifier.padding(top = 8.dp),
                                placeholder = { Text(text = "New Category Name...") },
                                onValueChange = {
                                    dialogInput.value = it
                                    dialogTitleState.value = ""
                                })
                        },
                        confirmButton = {

                            Button(
                                onClick = {

                                    if (dialogInput.value.isNotEmpty()) {
                                        onClick(dialogInput.value)
                                        openDialog.value = false
                                        dialogInput.value = ""
                                    } else {
                                        dialogTitleState.value = "Category name is empty"
                                    }

                                },
                                modifier = Modifier.width(100.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Pine,
                                    contentColor = Color.White
                                )
                            ) {

                                Text(text = "Add")

                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { openDialog.value = false },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Pine,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.width(100.dp)

                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .height(1.dp)
            )
        }

        items(
            items = categories,
            key = { it.categoryId }
        ) { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable{

                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.categoryTitle,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 16.dp,
                        bottom = 8.dp
                    )
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
                }
            }
        }
    }
}