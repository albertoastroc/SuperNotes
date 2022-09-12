package com.gmail.pentominto.us.supernotes.allnotesscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.ui.theme.LighterWalnutBrown

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
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp,
                top = 8.dp
            ),
        elevation = 1.dp,
        shape = RoundedCornerShape(4.dp),
        backgroundColor = LighterWalnutBrown

    ) {

        Text(
            text = note.noteTitle.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(
                    color = Color.Transparent
                ),
            fontSize = 20.sp
        )
    }
}

