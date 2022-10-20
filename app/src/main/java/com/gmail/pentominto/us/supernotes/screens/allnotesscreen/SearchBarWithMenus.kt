package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.utility.NoRippleInteractionSource


@Composable
fun SearchBarWithMenu(
    input : String,
    onInputChange : (String) -> Unit,
    onMenuIconClick : () -> Unit
) {

    val customTextSelectionColors = TextSelectionColors(
        handleColor = Color.Transparent,
        backgroundColor = Color.Transparent
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.secondaryVariant),
        ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        top = 20.dp,
                        bottom = 20.dp
                    )
                    .clickable(
                        interactionSource = NoRippleInteractionSource(),
                        onClick = {

                            onMenuIconClick()
                        },
                        indication = null
                    ),
                tint = MaterialTheme.colors.onSecondary
            )

            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {

                TextField(
                    modifier = Modifier
                        .weight(1f),
                    value = input,
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.onSecondary,
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
                    textStyle = TextStyle.Default.copy(
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSecondary
                    )
                )
            }
        }

    }
}