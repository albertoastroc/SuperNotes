package com.gmail.pentominto.us.supernotes.allnotesscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.data.Note

@Composable
fun NoteItem(
    note : Note,
    modifier : Modifier,
    onClick : (Long) -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick(note.noteId)
            }
            .padding(4.dp),
        elevation = 1.dp,
        shape = RoundedCornerShape(4.dp),
        backgroundColor = MaterialTheme.colors.secondary

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        2.dp,
                        MaterialTheme.colors.secondaryVariant
                    )
                )
        ) {

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Divider(
                modifier = Modifier.height(1.dp)
                    .padding(horizontal = 12.dp),
                color = MaterialTheme.colors.secondaryVariant
            )

            Text(
                text = note.noteTitle.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    )
                    .background(
                        color = Color.Transparent
                    ),
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colors.onBackground
            )
            Divider(
                modifier = Modifier.height(1.dp)
                    .padding(horizontal = 12.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
        }
    }
}

