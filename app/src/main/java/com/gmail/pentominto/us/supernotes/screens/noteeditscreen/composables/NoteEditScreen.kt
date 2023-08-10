package com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.composables.NavigationDrawerOptions
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditScreenViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteEditScreen(
    noteid: Int,
    viewModel: NoteEditScreenViewModel = hiltViewModel(),
    onDrawerItemClick: (Int) -> Unit

) {

    val shouldShowDateTimePickerDialogue =  remember { mutableStateOf(false) }

    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val noteState by remember { viewModel.noteEditState }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = .5f)
    )

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed),
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )

    DisposableEffect(lifeCycleOwner) {

        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_STOP,
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_DESTROY
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
            .background(MaterialTheme.colors.background),
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = Color.Transparent,
        sheetGesturesEnabled = true,
        drawerGesturesEnabled = true,
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            NavigationDrawerOptions(
                drawerState = bottomSheetScaffoldState.drawerState,
                onDrawerItemClick = onDrawerItemClick
            )
        },
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

//                if (shouldShowDateTimePickerDialogue.value){
//
//                    DateTimePickerDialogue(
//                        context = context,
//                        shouldShowDateTimePickerDialogue = shouldShowDateTimePickerDialogue
//                    )
//
//                }


                TitleAndMenuCard(
                    customTextSelectionColors = customTextSelectionColors,
                    noteState = noteState,
                    onTitleValueChange = viewModel::onTitleInputChange,
                    scheduleReminder = viewModel::scheduleReminder,
                    bottomSheetScaffoldState = bottomSheetScaffoldState
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
                currentCategory = noteState.currentChosenCategory,
                onAddCategory = viewModel::insertCategory,
                onDeleteCategory = viewModel::deleteCategory
            ) {

                coroutineScope.launch {
                    viewModel.onCategoryChange(it)
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }
    )
}

@Composable
fun DateTimePickerDialogue(
    context : Context,
    shouldShowDateTimePickerDialogue : MutableState<Boolean>,
    scheduleReminder : (Context, Long) -> Unit
){

    val calendar = Calendar.getInstance()

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )

    val timePicker = TimePickerDialog(
        context,
        { view, selectedHour: Int, selectedMinute: Int ->
            calendar.set(
                datePicker.datePicker.year,
                datePicker.datePicker.month,
                datePicker.datePicker.dayOfMonth,
                selectedHour,
                selectedMinute
            )
        },
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE],
        false
    )

    Dialog(onDismissRequest = {
        shouldShowDateTimePickerDialogue.value = false
       }
    ) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                contentAlignment = Alignment.Center){

                Column() {


                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "9:00 AM",
                            color = Color.Black
                        )

                        IconButton(onClick = {

                            timePicker.show()

                        }) {
                            Icon(Icons.Default.Edit,
                                contentDescription = "icon",
                                tint = Color.Black
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "12/01/23",
                            color = Color.Black
                        )

                        IconButton(onClick = {
                            datePicker.show()
                        }) {
                            Icon(Icons.Default.Edit,
                                contentDescription = "icon",
                                tint = Color.Black
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {

                            scheduleReminder(context, calendar.timeInMillis)

                        }) {
                            Text(text = "Set reminder")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                }

            }
        }

    }


}
