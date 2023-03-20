package com.gmail.pentominto.us.supernotes.screens.trashnotescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.screens.readonlynotescreen.ReadOnlyNoteViewModel

@Composable
fun ReadOnlyNoteScreen(
    trashNoteId: Int,
    viewModel: ReadOnlyNoteViewModel = hiltViewModel()
) {
    val trashNoteState = remember { viewModel.readOnlyNoteState }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
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
                    text = trashNoteState.value.trashNote?.noteTitle ?: "",
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            bottom = 20.dp,
                            start = 24.dp
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
                text = trashNoteState.value.trashNote?.noteBody ?: "",
                color = MaterialTheme.colors.onBackground,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(24.dp)
            )
        }
    }
}
