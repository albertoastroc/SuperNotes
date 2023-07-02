@file:OptIn(ExperimentalMaterialApi::class)

package com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditScreenViewModel
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditState
import com.gmail.pentominto.us.supernotes.utility.NoRippleInteractionSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun NoteEditScreen(
    noteid: Int,
    viewModel: NoteEditScreenViewModel = hiltViewModel()

) {
    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val noteState by remember { viewModel.noteEditState }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = .5f)
    )

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_PAUSE
                -> {
                    viewModel.saveNoteText()
                }
                else -> {}
            }
        }

        lifeCycleOwner.addObserver(observer)

        onDispose {
            lifeCycleOwner.removeObserver(observer)
        }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .testTag("Note Edit Screen"),
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = Color.Transparent,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp
                    )
            ) {
                TitleCard(
                    customTextSelectionColors = customTextSelectionColors,
                    noteState = noteState,
                    onTitleValueChange = viewModel::onTitleInputChange,
                    setAlarm = viewModel::setAlarm,
                    context = context,
                    coroutineScope = coroutineScope,
                    bottomSheetScaffoldState = bottomSheetScaffoldState
                )

                Divider(
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .height(1.dp)
                )

                BodyCard(
                    customTextSelectionColors = customTextSelectionColors,
                    noteState = noteState,
                    onBodyValueChange = viewModel::onBodyInputChange
                )
            }
        },

        sheetContent = {
            CategoriesList(
                categories = noteState.categories,
                currentCategory = noteState.noteCategory,
                onClickDialog = { viewModel.insertCategory(it) },
                onDeleteCategory = { viewModel.deleteCategory(it) }
            ) {
                viewModel.saveNoteCategory(it)
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }
    )
}

@Composable
private fun BodyCard(
    customTextSelectionColors: TextSelectionColors,
    noteState: NoteEditState,
    onBodyValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        elevation = 1.dp,
        shape = RoundedCornerShape(2.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides customTextSelectionColors
        ) {
            TextField(
                value = noteState.noteBody,
                placeholder = {
                    Text(
                        text = "Enter Text...",
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 18.sp
                    )
                },
                onValueChange = { onBodyValueChange(it) },
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        top = 8.dp
                    )
                    .fillMaxWidth()
                    .background(
                        color = Color.Transparent
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colors.onPrimary
                ),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 18.sp
                )
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun TitleCard(
    customTextSelectionColors: TextSelectionColors,
    noteState: NoteEditState,
    onTitleValueChange: (String) -> Unit,
    context: Context,
    setAlarm : (Context, Long) -> Unit,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    val clipboardManager = LocalClipboardManager.current

    val focusManager = LocalFocusManager.current

    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val datePicker = DatePickerDialog(
        context,
        { datePicker : DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
        }, year, month, dayOfMonth
    )

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
           calendar.set(datePicker.datePicker.year, datePicker.datePicker.month, datePicker.datePicker.dayOfMonth, selectedHour, selectedMinute)
        }, hour, minute, false
    )

    datePicker.datePicker.minDate = calendar.timeInMillis


    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(2.dp),
        elevation = 1.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalTextSelectionColors provides customTextSelectionColors
            ) {
                TextField(
                    value = noteState.noteTitle,
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Enter a Title...",
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 18.sp
                        )
                    },
                    onValueChange = { onTitleValueChange(it) },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                        .background(
                            color = Color.Transparent
                        ),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.onPrimary
                    ),
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 18.sp
                    )

                )
            }

            Column {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable(
                            interactionSource = NoRippleInteractionSource(),
                            onClick = {
                                dropDownMenuExpanded = true
                            },
                            indication = null
                        ),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )

                DropdownMenu(
                    expanded = dropDownMenuExpanded,
                    onDismissRequest = { dropDownMenuExpanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        clipboardManager.setText(
                            AnnotatedString(
                                noteState.noteBody
                            )
                        )
                        dropDownMenuExpanded = false
                        Toast.makeText(
                            context,
                            "Text Copied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Text(text = "Copy to clipboard")
                    }

                    DropdownMenuItem(onClick = {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                            focusManager.clearFocus()
                            dropDownMenuExpanded = false
                        }
                    }) {
                        Text(text = "Set category")
                    }

                    DropdownMenuItem(onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                noteState.noteBody
                            )
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(
                            sendIntent,
                            null
                        )

                        context.startActivity(shareIntent)
                        dropDownMenuExpanded = false
                    }) {
                        Text(text = "Share")
                    }

                    DropdownMenuItem(onClick = {
                        datePicker.show()
                        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->

                            timePicker.show()
                            timePicker.setOnDismissListener {

                                setAlarm(context, calendar.timeInMillis)
                                Log.d("TAG",
                                    "TitleCard: ${calendar.time}"
                                )
                            }
                        }
                    }) {
                        Text(text = "Set reminder")
                    }
                }
            }
        }
    }
}
