package com.gmail.pentominto.us.supernotes.noteeditscreen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.Utility.NoRippleInteractionSource
import com.gmail.pentominto.us.supernotes.data.Category
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
)

@Composable
fun NoteEditScreen(
    noteId : Long,
    viewModel : NoteEditScreenViewModel = hiltViewModel()

) {

    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current

    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val focusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    val note by remember { viewModel.note }

    val categories by remember { viewModel.categories }

    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = .5f)
    )

    DisposableEffect(lifeCycleOwner) {

        val observer = LifecycleEventObserver { _, event ->

            when (event) {

                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP,
                Lifecycle.Event.ON_DESTROY -> viewModel.saveNoteText()
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
            .background(MaterialTheme.colors.background)
            .fillMaxSize(),
        scaffoldState = bottomSheetScaffoldState,
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

                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(2.dp),
                    elevation = 1.dp,
                    backgroundColor = MaterialTheme.colors.primary
                ) {

                    Row(
                        modifier = Modifier
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {

                            TextField(
                                value = note.noteTitle.toString(),
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        text = "Enter a Title...",
                                        color = MaterialTheme.colors.onPrimary,
                                        fontSize = 18.sp
                                    )
                                },
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
                                    disabledIndicatorColor = Color.Transparent,
                                    cursorColor = MaterialTheme.colors.onPrimary
                                ),
                                textStyle = TextStyle(
                                    color = MaterialTheme.colors.onBackground,
                                    fontSize = 18.sp
                                )
                            )
                        }

                        Column() {

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
                                onDismissRequest = { dropDownMenuExpanded = false },
                                offset = DpOffset(
                                    x = 0.dp,
                                    y = 4.dp
                                )
                            ) {

                                DropdownMenuItem(onClick = {

                                    clipboardManager.setText(
                                        AnnotatedString(
                                            note.noteBody ?: ""
                                        )
                                    )
                                    dropDownMenuExpanded = false
                                    viewModel.getCurrentDate()
                                    Toast.makeText(
                                        context,
                                        "Copied to clipboard",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }) {
                                    Text(text = "Copy to clipboard")
                                }

                                DropdownMenuItem(onClick = {
                                    coroutineScope.launch {

                                        bottomSheetState.expand()
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
                                            note.noteBody ?: ""
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
                            }
                        }
                    }
                }

                Divider(
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Card(
                    modifier = Modifier
                        .weight(1f),
                    elevation = 1.dp,
                    shape = RoundedCornerShape(2.dp),
                    backgroundColor = MaterialTheme.colors.primary
                ) {

                    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {

                        TextField(
                            value = note.noteBody.toString(),
                            placeholder = {
                                Text(
                                    text = "Enter Text...",
                                    color = MaterialTheme.colors.onPrimary,
                                    fontSize = 18.sp
                                )
                            },
                            onValueChange = { viewModel.onBodyInputChange(it) },
                            modifier = Modifier
                                .focusRequester(focusRequester)
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
        },

        sheetContent = {

            CategoriesList(
                categories = categories,
                currentCategory = viewModel.noteCategory.value,
                onClickDialog = { viewModel.insertCategory(it) },
                onDeleteCategory = { viewModel.deleteCategory(it) },
                onClickCategory = {

                    viewModel.saveNoteCategory(it)
                    coroutineScope.launch {

                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesList(
    categories : List<Category>,
    currentCategory : Category,
    onClickDialog : (String) -> Unit,
    onDeleteCategory : (Category) -> Unit,
    onClickCategory : (Category) -> Unit,
) {

    val openCategoryDialog = remember { mutableStateOf(false) }

    val dialogInput = remember { mutableStateOf("") }

    val dialogTitleState = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 450.dp)
    ) {

        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.secondaryVariant)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource = NoRippleInteractionSource(),
                            indication = null,
                            onClick = {
                                openCategoryDialog.value = true
                            }
                        ),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground)
                )

                if (openCategoryDialog.value) {

                    AlertDialog(

                        modifier = Modifier.width(400.dp),
                        onDismissRequest = { openCategoryDialog.value = false },
                        title = {
                            Text(
                                text = dialogTitleState.value
                            )

                        },
                        text = {

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                TextField(
                                    value = dialogInput.value,
                                    modifier = Modifier
                                        .padding(top = 8.dp),
                                    placeholder = { Text(text = "New Category Name...") },
                                    onValueChange = {
                                        dialogInput.value = it
                                        dialogTitleState.value = ""
                                    },
                                )
                            }

                        },
                        buttons = {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Button(
                                    onClick = {

                                        if (dialogInput.value.isNotEmpty()) {
                                            onClickDialog(dialogInput.value)
                                            openCategoryDialog.value = false
                                            dialogInput.value = ""
                                        } else {
                                            dialogTitleState.value = "Category name is empty"
                                        }
                                    },
                                    modifier = Modifier.width(100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Color.White
                                    )
                                ) {

                                    Text(text = "Add")

                                }


                                Button(
                                    onClick = { openCategoryDialog.value = false },
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.width(100.dp)

                                ) {
                                    Text(text = "Cancel")
                                }
                            }

                        }
//                        confirmButton = {
//                            Button(
//                                onClick = {
//
//                                    if (dialogInput.value.isNotEmpty()) {
//                                        onClickDialog(dialogInput.value)
//                                        openCategoryDialog.value = false
//                                        dialogInput.value = ""
//                                    } else {
//                                        dialogTitleState.value = "Category name is empty"
//                                    }
//                                },
//                                modifier = Modifier.width(100.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    contentColor = Color.White
//                                )
//                            ) {
//
//                                Text(text = "Add")
//
//                            }
//                        },
//                        dismissButton = {
//                            Button(
//                                onClick = { openCategoryDialog.value = false },
//                                colors = ButtonDefaults.buttonColors(
//                                    contentColor = Color.White
//                                ),
//                                modifier = Modifier.width(100.dp)
//
//                            ) {
//                                Text(text = "Cancel")
//                            }
//                        }
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .height(.5.dp),
                color = MaterialTheme.colors.onPrimary
            )
        }

        items(
            items = categories,
            key = { it.categoryId }
        ) { category ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (category == currentCategory)
                            MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant
                    )
                    .height(60.dp)
                    .clickable {

                        onClickCategory(category)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.categoryTitle,
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            start = 16.dp,
                            bottom = 8.dp
                        )
                        .weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                if (category.categoryTitle != "No Category") {

                    Icon(
                        painterResource(id = R.drawable.ic_baseline_delete_24),
                        modifier = Modifier
                            .padding(
                                end = 16.dp,
                                start = 8.dp
                            )
                            .clickable(
                                interactionSource = NoRippleInteractionSource(),
                                onClick = {

                                    onDeleteCategory(category)
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