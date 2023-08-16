package com.gmail.pentominto.us.supernotes.screens.readonlynotescreen.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.composables.NavigationDrawerOptionsContainer
import com.gmail.pentominto.us.supernotes.screens.readonlynotescreen.ReadOnlyNoteViewModel
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReadOnlyNoteScreen(
    trashNoteId : Int,
    viewModel : ReadOnlyNoteViewModel = hiltViewModel(),
    onDrawerItemClick : (Int) -> Unit
) {
    val trashNoteState = remember { viewModel.readOnlyNoteState }

    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = true,
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            NavigationDrawerOptionsContainer(
                drawerState = scaffoldState.drawerState,
                onDrawerItemClick = onDrawerItemClick
            )
        }
    ) {

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
}