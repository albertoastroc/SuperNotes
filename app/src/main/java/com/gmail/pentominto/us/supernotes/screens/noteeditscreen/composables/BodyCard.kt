package com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditState

@Composable
fun BodyCard(
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
