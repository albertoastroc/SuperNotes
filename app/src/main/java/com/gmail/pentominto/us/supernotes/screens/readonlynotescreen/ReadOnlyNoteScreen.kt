package com.gmail.pentominto.us.supernotes.screens.trashnotescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun ReadOnlyNoteScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Title thing",
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            bottom = 20.dp,
                            start = 24.dp,
                        )
                )
            }
        }

        Divider(
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(horizontal = 2.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(2.dp),
            elevation = 1.dp,
            backgroundColor = MaterialTheme.colors.primary
        ) {

            Text(
                text = "Title thing",
                color = MaterialTheme.colors.onBackground,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(24.dp)
            )

        }
    }
}