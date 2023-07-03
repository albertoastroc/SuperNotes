package com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.utility.Constants.DEFAULT_CATEGORY
import com.gmail.pentominto.us.supernotes.utility.NoRippleInteractionSource

@Composable
fun CategoriesList(
    categories: List<Category>,
    currentCategory: String,
    onAddCategory: (String) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onSelectCategory: (Category) -> Unit
) {
    val openCategoryDialog = remember { mutableStateOf(false) }

    val dialogInput = remember { mutableStateOf(String()) }

    val dialogTitleState = remember { mutableStateOf(String()) }

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .heightIn(max = 450.dp)
        ) {
            item {
                AddItemRow(openCategoryDialog)

                if (openCategoryDialog.value) {
                    AddCategoryDialog(
                        openCategoryDialog = openCategoryDialog,
                        dialogTitleState = dialogTitleState,
                        dialogInput = dialogInput,
                        onClickDialog = onAddCategory
                    )
                }
            }

            items(
                items = categories,
                key = { it.categoryId }
            ) { category ->

                CategoryItem(
                    category = category,
                    currentCategory = currentCategory,
                    onClickCategory = onSelectCategory,
                    onDeleteCategory = onDeleteCategory
                )
            }
        }
    }
}

@Composable
private fun AddItemRow(openCategoryDialog: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.secondaryVariant),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = null,
            modifier = Modifier
                .padding(20.dp)
                .clickable(
                    interactionSource = NoRippleInteractionSource(),
                    indication = null,
                    onClick = {
                        openCategoryDialog.value = true
                    }
                ),
            tint = MaterialTheme.colors.onBackground
        )
    }

    Divider(
        modifier = Modifier
            .height(1.dp),
        color = MaterialTheme.colors.onPrimary
    )
}

@Composable
private fun AddCategoryDialog(
    openCategoryDialog: MutableState<Boolean>,
    dialogTitleState: MutableState<String>,
    dialogInput: MutableState<String>,
    onClickDialog: (String) -> Unit
) {
    AlertDialog(
        modifier = Modifier.width(400.dp),
        onDismissRequest = { openCategoryDialog.value = false },
        backgroundColor = MaterialTheme.colors.background,
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
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = MaterialTheme.colors.onPrimary
                    )
                )
            }
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Button(
                    onClick = { openCategoryDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.onSecondary,
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    modifier = Modifier.width(100.dp)

                ) {
                    Text(text = "Cancel")
                }

                Button(
                    onClick = {
                        if (dialogInput.value.isNotEmpty()) {
                            onClickDialog(dialogInput.value)
                            openCategoryDialog.value = false
                            dialogInput.value = ""
                        } else {
                            dialogTitleState.value = "Note Category name is empty"
                        }
                    },
                    modifier = Modifier.width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.onSecondary,
                        backgroundColor = MaterialTheme.colors.secondary
                    )
                ) {
                    Text(text = "Add")
                }
            }
        }
    )
}

@Composable
private fun CategoryItem(
    category: Category,
    currentCategory: String,
    onClickCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (category.categoryTitle == currentCategory) {
                    MaterialTheme.colors.secondary
                } else {
                    MaterialTheme.colors.secondaryVariant
                }
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
            maxLines = 2,
            color = MaterialTheme.colors.onBackground
        )

        if (category.categoryTitle != DEFAULT_CATEGORY) {
            Icon(
                painterResource(id = R.drawable.ic_baseline_delete_24),
                modifier = Modifier
                    .padding(20.dp)
                    .clickable(
                        interactionSource = NoRippleInteractionSource(),
                        onClick = {
                            onDeleteCategory(category)
                        },
                        indication = null
                    ),
                contentDescription = null
            )
        }
    }
}
