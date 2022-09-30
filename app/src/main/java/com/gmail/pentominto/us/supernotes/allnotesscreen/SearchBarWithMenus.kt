package com.gmail.pentominto.us.supernotes.allnotesscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.Utility.NoRippleInteractionSource


@Composable
fun SearchBarWithMenu(
    input : String,
    onInputChange : (String) -> Unit,
    onMenuIconClick : () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
            .clip(CircleShape)
            .background(MaterialTheme.colors.secondaryVariant),

        ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painterResource(id = R.drawable.ic_baseline_menu_24),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable(
                        interactionSource = NoRippleInteractionSource(),
                        onClick = {

                            onMenuIconClick()
                        },
                        indication = null
                    ),
                contentDescription = null,
                tint = MaterialTheme.colors.onSecondary
            )

            TextField(
                modifier = Modifier.weight(1f),
                value = input,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colors.secondaryVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Search Notes...",
                        color = MaterialTheme.colors.onSecondary,
                        fontSize = 16.sp
                    )
                },
                onValueChange = { onInputChange(it) },
                textStyle = TextStyle.Default.copy(fontSize = 16.sp)
            )
        }
    }
}