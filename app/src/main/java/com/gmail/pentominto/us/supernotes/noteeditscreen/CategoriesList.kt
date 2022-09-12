package com.gmail.pentominto.us.supernotes.noteeditscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.Utility.LogCompositions
import com.gmail.pentominto.us.supernotes.Utility.NoRippleInteractionSource
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.ui.theme.LimishGreen

@Composable
fun CategoriesList(
    categories : List<Category>
) {
    
    LogCompositions(
        tag = "TAG",
        msg = "CategoriesList"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp),
    ) {
        item() {
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

                        }

                ) {

                    Row(
                        modifier = Modifier
                            .background(LimishGreen)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End,
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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
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

                    Checkbox(
                        checked = true,
                        modifier = Modifier.padding(end = 8.dp),
                        onCheckedChange = {

                        }
                    )
                }

            }
        }
    }
}

@Composable
fun CategoryAlertDialog(
    dialogState : Boolean
) {

    if (dialogState) {
        AlertDialog(
            onDismissRequest = {
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