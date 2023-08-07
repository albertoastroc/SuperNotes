package com.gmail.pentominto.us.supernotes.screens.homenotesscreen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.data.Note

@Composable
fun NoteContentContainer(
    note: Note,
    modifier: Modifier,
    onClick: (Int) -> Unit
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
        backgroundColor = MaterialTheme.colors.primary

    ) {
        Column {
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .padding(horizontal = 12.dp),
                color = MaterialTheme.colors.primaryVariant
            )

            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                2.dp,
                                MaterialTheme.colors.primary
                            )
                        )
                ) {
                    Text(
                        text = note.noteTitle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 24.dp,
                                end = 12.dp,
                                top = 12.dp
                            )
                            .background(
                                color = Color.Transparent
                            ),
                        fontSize = 22.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        color = MaterialTheme.colors.onBackground
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Divider(
                                modifier = Modifier
                                    .height(1.dp)
                                    .padding(horizontal = 12.dp),
                                color = MaterialTheme.colors.primaryVariant

                            )
                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(
                                    end = 12.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                ),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Created ${note.createdDate}",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}
