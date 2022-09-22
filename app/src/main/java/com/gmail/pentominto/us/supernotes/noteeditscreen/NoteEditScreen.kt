package com.gmail.pentominto.us.supernotes.noteeditscreen

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.Utility.NoRippleInteractionSource
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.ui.theme.Pine
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
)
@Composable
fun NoteEditScreen(
    noteId : Long,
    viewModel : NoteEditScreenViewModel = hiltViewModel()

) {

    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    val configuration = LocalConfiguration.current

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val focusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    val note by remember { viewModel.note }

    val categories by remember { viewModel.categories }



    if (noteId != 0L) {
        viewModel.getNote(noteId)
    } else {
        viewModel.insertNewNote()
    }

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
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
        scaffoldState = bottomSheetScaffoldState,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        content = {

            Column(
                modifier = Modifier.fillMaxWidth()
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

                        TextField(
                            value = note.noteTitle.toString(),
                            singleLine = true,
                            placeholder = { Text(
                                text = "Enter a Title...",
                                color = MaterialTheme.colors.onPrimary,
                                fontSize = 18.sp
                            ) },
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
                            ),
                            textStyle = TextStyle(
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 18.sp
                            )
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
                            tint = MaterialTheme.colors.onBackground
                        )
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

                    TextField(
                        value = note.noteBody.toString(),
                        placeholder = { Text(
                            text = "Enter Text...",
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 18.sp
                        ) },
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
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.onBackground,
                            fontSize = 18.sp
                        )
                    )
                }

                Divider(
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {

                        Card(
                            modifier = Modifier,
                            elevation = 0.dp,
                            shape = RoundedCornerShape(2.dp),
                            backgroundColor = MaterialTheme.colors.primary
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
                                        coroutineScope.launch {

                                            bottomSheetState.expand()
                                            focusManager.clearFocus()
                                        }
                                    },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .widthIn(max = 200.dp)
                                        .heightIn(max = 100.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                                ) {
                                    Text(
                                        text = viewModel.noteCategory.value.categoryTitle,
                                        modifier = Modifier
                                            .padding(
                                                top = 8.dp,
                                                bottom = 8.dp,
                                                start = 12.dp,
                                                end = 12.dp
                                            ),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        color = MaterialTheme.colors.onSecondary
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier
                                ) {

                                    Text(
                                        text = "Created : 4/13/22",
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colors.onBackground
                                    )

                                    Text(
                                        text = "Last Modified : 5/1/22",
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colors.onBackground
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
                    .background(MaterialTheme.colors.primaryVariant)
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
                        onDismissRequest = { openCategoryDialog.value = false },
                        title = {
                            Text(
                                text = dialogTitleState.value
                            )
                        },
                        text = {
                            TextField(
                                value = dialogInput.value,
                                modifier = Modifier
                                    .padding(top = 8.dp),
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
                                        onClickDialog(dialogInput.value)
                                        openCategoryDialog.value = false
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
                                onClick = { openCategoryDialog.value = false },
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
                            MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
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