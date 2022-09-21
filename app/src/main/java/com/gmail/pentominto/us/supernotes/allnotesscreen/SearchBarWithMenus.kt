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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gmail.pentominto.us.supernotes.R
import com.gmail.pentominto.us.supernotes.Utility.NoRippleInteractionSource
import com.gmail.pentominto.us.supernotes.ui.theme.LightLimeGreen

@Composable
fun SearchBarWithMenu(
    input : String,
    onInputChange : (String) -> Unit,
    scaffoldState : ScaffoldState,
    onMenuIconClick : () -> Unit
) {

    val searchBarBackGroundColor = LightLimeGreen
    val scope = rememberCoroutineScope()
    //var menuExpanded = remember { mutableStateOf(false) }

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
            .background(MaterialTheme.colors.primaryVariant),

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
                tint = MaterialTheme.colors.onBackground
            )

            TextField(
                modifier = Modifier.weight(1f),
                value = input,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Search Notes...",
                        color = MaterialTheme.colors.onBackground
                    )
                },
                onValueChange = { onInputChange(it) },
            )

//            Column() {
//
//                Icon(
//                    painterResource(id = R.drawable.ic_baseline_more_vert_24),
//                    modifier = Modifier
//                        .padding(end = 20.dp)
//                        .clickable(
//                            interactionSource = NoRippleInteractionSource(),
//                            onClick = {
//                                menuExpanded.value = ! menuExpanded.value
//                            },
//                            indication = null
//                        ),
//                    contentDescription = null,
//                )
//
//                DropdownMenu(
//                    expanded = menuExpanded.value,
//                    offset = DpOffset(
//                        x = 0.dp,
//                        y = 8.dp
//                    ),
//                    onDismissRequest = { menuExpanded.value = false }) {
//
//                    DropdownMenuItem(onClick = { /*TODO*/ }) {
//                        Text(text = "Item 1")
//                    }
//
//                    DropdownMenuItem(onClick = { /*TODO*/ }) {
//                        Text(text = "Item 2")
//                    }
//                    DropdownMenuItem(onClick = { /*TODO*/ }) {
//                        Text(text = "Item 3")
//                    }
//                }
//            }
        }
    }
}