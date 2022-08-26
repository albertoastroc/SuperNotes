package com.gmail.pentominto.us.supernotes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.pentominto.us.supernotes.ui.theme.LighterWalnutBrown
import com.gmail.pentominto.us.supernotes.ui.theme.PastelGreen
import com.gmail.pentominto.us.supernotes.ui.theme.Pine
import com.gmail.pentominto.us.supernotes.ui.theme.Powder

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AllNotesScreen() {

    val searchBarBackGroundColor = PastelGreen

    val notesList = listOf(
        "Shopping List",
        "Homeworks tips",
        "Movies",
        "Appointments",
        "Rando",
        "Planes",
        "Cars",
        "Good lak",
        "Tropoya",
        "Frozen"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Powder,
        topBar = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(searchBarBackGroundColor),

                ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painterResource(id = R.drawable.ic_baseline_menu_24),
                        modifier = Modifier.padding(start = 20.dp),
                        contentDescription = null,
                    )

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = "Search notes...",
                        colors = TextFieldDefaults.textFieldColors(
                            disabledTextColor = Color.Transparent,
                            backgroundColor = searchBarBackGroundColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        onValueChange = {}
                    )
                }
            }
        },
        content = {

            LazyColumn {
                items(notesList) { note ->
                    NoteItem(noteTitle = note)
                }
            }

        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = { /*TODO*/ },
                backgroundColor = Pine,
                content = {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                    )
                },
                contentColor = Color.White
            )

        }
    )
}

@Composable
fun NoteItem(
    noteTitle : String
) {

    Card(
        modifier = Modifier
            .fillMaxSize()
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

//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color.White,
//                            LighterWalnutBrown
//                        ),
//                        startY = 0f,
//                        tileMode = TileMode.Decal
//                    )
//                )
//        ) {

        Text(
            text = noteTitle,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .background(
                    color = Color.Transparent
                ),
            fontSize = 20.sp
        )
    }

}

