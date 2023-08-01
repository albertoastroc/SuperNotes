package com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gmail.pentominto.us.supernotes.R

@Composable
fun AllNotesFab(onNoteClick: (Int) -> Unit) {
    FloatingActionButton(
        content = {
            Icon(
                painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = null,
                tint = MaterialTheme.colors.onSecondary
            )
        },
        onClick = { onNoteClick(0) },
        backgroundColor = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(12.dp)
    )
}
