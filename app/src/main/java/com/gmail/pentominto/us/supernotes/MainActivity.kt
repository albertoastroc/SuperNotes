

package com.gmail.pentominto.us.supernotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gmail.pentominto.us.NoteEditScreen
import com.gmail.pentominto.us.supernotes.ui.theme.SuperNotesTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperNotesTheme {

                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = true
                    )
                }
                SuperNotesApp()
            }
        }
    }
}

@Composable
fun SuperNotesApp() {

    val navController = rememberNavController()

   NavHost(
        navController = navController,
        startDestination = "allNotes",

    ) {
        composable(
            "allNotes"
        ) {

            AllNotesScreen { noteId ->
                navController.navigate("noteEdit/${noteId}")
            }
        }

        composable(
            "noteEdit/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                }
            ),
        ) {

            val noteId = remember {
                it.arguments?.getLong("noteId")
            }
            if (noteId != null) {
                NoteEditScreen(noteId = noteId)
            }
        }
    }
}

/**
 *
 * Database for notes
 * needs to be crossreferenced with category one to many
 * Opens new note automatically? does not create note if there is no title
 *
 * Category defaults to other if no category given
 * if category becomes empty it is not displayed
 * put trash button in drawer next to categories
 * move notes that were inside that category into others
 *
 * select notes delete
 *
 * if the focus is on the note body or title while categories are active, close categories
 *
 * reuse categories list as a composable, see what composables i can reuse and use logging
 * try using weight instead of extra containers
 * use by to not have to use .value
 *
 */
