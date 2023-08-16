package com.gmail.pentominto.us.supernotes.screens.homenotesscreen.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.data.Category

@Composable
fun CategoryHeader(
    category : Category,
    modifier : Modifier
) {
    Text(
        text = category.categoryTitle,
        modifier = modifier
            .padding(
                start = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        color = MaterialTheme.colors.onBackground,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 22.sp
    )
}
