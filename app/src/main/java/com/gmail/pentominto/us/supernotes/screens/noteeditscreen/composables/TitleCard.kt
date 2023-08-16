package com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.NotificationManagerCompat
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditState
import com.gmail.pentominto.us.supernotes.utility.NoRippleInteractionSource
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(
    ExperimentalMaterialApi::class
)
@SuppressLint("MissingPermission")
@Composable
fun TitleAndMenuCard(
    customTextSelectionColors : TextSelectionColors,
    noteState : NoteEditState,
    onTitleValueChange : (String) -> Unit,
    scheduleReminder : (Context, Long) -> Unit,
    bottomSheetScaffoldState : BottomSheetScaffoldState
) {
    val context = LocalContext.current

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

            NoteEditMenu(
                noteState = noteState,
                // showReminderSchedulerDialog = showAlarmDialogs,
                scheduleReminder = scheduleReminder,
                context = context,
                bottomSheetScaffoldState = bottomSheetScaffoldState
                //  launcher = launcher
            )
        }
    }

    Divider(
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .height(1.dp)
    )
}

@OptIn(
    ExperimentalMaterialApi::class
)
@Composable
private fun NoteEditMenu(
    noteState : NoteEditState,
    scheduleReminder : (Context, Long) -> Unit,
    context : Context,
    bottomSheetScaffoldState : BottomSheetScaffoldState
) {
    val shouldShowDateTimePickerDialogue = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val packageManager = LocalContext.current.packageManager

    val focusManager = LocalFocusManager.current

    val clipboardManager = LocalClipboardManager.current

    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            shouldShowDateTimePickerDialogue.value = true
        }
    }

    if (shouldShowDateTimePickerDialogue.value) {
        DateTimePickerDialogue(
            context = context,
            shouldShowDateTimePickerDialogue = shouldShowDateTimePickerDialogue,
            scheduleReminder = scheduleReminder
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
                val sendIntent : Intent = Intent().apply {
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
                coroutineScope.launch {
                    requestNotificationPermission(
                        packageManager,
                        context,
                        bottomSheetScaffoldState,
                        launcher
                    )
                }
                dropDownMenuExpanded = false
            }) {
                Text(text = "Set reminder")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private suspend fun requestNotificationPermission(
    packageManager : PackageManager,
    context : Context,
    bottomSheetScaffoldState : BottomSheetScaffoldState,
    launcher : ManagedActivityResultLauncher<String, Boolean>
) {
    val notificationManager : NotificationManagerCompat = NotificationManagerCompat.from(context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (packageManager.checkPermission(
                Manifest.permission.POST_NOTIFICATIONS,
                context.packageName
            ) == PackageManager.PERMISSION_GRANTED &&
            notificationManager.getNotificationChannel("1")?.importance != NotificationManager.IMPORTANCE_NONE
        ) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else if (packageManager.checkPermission(
                Manifest.permission.POST_NOTIFICATIONS,
                context.packageName
            ) == PackageManager.PERMISSION_GRANTED &&
            notificationManager.getNotificationChannel("1")?.importance == NotificationManager.IMPORTANCE_NONE ||
            (
                    packageManager.checkPermission(
                        Manifest.permission.POST_NOTIFICATIONS,
                        context.packageName
                    ) == PackageManager.PERMISSION_DENIED
                    )
        ) {
            val snackBarResult = bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                "Please allow notifications to use reminders.",
                "Go to Permissions",
                SnackbarDuration.Long
            )

            if (snackBarResult == SnackbarResult.ActionPerformed) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                intent.putExtra(
                    "android.provider.extra.APP_PACKAGE",
                    context.applicationInfo.packageName
                )

                context.startActivity(intent)
            }
        } else {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    } else {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
fun DateTimePickerDialogue(
    context : Context,
    shouldShowDateTimePickerDialogue : MutableState<Boolean>,
    scheduleReminder : (Context, Long) -> Unit
) {
    val calendar = Calendar.getInstance()

    val dateFormatter = SimpleDateFormat("MM-dd-yyyy")
    val date = dateFormatter.format(calendar.time)

    val timeFormatter = SimpleDateFormat("hh:mm aa")
    val time = timeFormatter.format(calendar.time)

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year,
                month,
                dayOfMonth
            )
        },

        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )

    val timePicker = TimePickerDialog(
        context,
        { view, selectedHour : Int, selectedMinute : Int ->
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

    val dateState = remember { mutableStateOf(date) }
    val timeState = remember { mutableStateOf(time) }

    Dialog(
        onDismissRequest = {
            shouldShowDateTimePickerDialogue.value = false
        }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colors.primary
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    // time
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = dateState.value,
                            color = MaterialTheme.colors.onPrimary
                        )

                        IconButton(onClick = {
                            datePicker.show()
                        }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "icon",
                                tint = MaterialTheme.colors.onPrimary
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
                            text = timeState.value,
                            color = MaterialTheme.colors.onPrimary
                        )

                        IconButton(onClick = {
                            timePicker.show()
                        }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "icon",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                            onClick = {
                                scheduleReminder(context,
                                    calendar.timeInMillis
                                )
                                shouldShowDateTimePickerDialogue.value = false
                            }
                        ) {
                            Text(
                                text = "Set reminder",
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
