

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
import com.gmail.pentominto.us.supernotes.noteeditscreen.NoteEditScreen
import com.gmail.pentominto.us.supernotes.optionsscreen.OptionsScreen
import com.gmail.pentominto.us.supernotes.ui.theme.SuperNotesTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(){

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

            AllNotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate("noteEdit/${noteId}")
                },
                onOptionsClick = { menuItemId ->

                    when (menuItemId) {

                        2 -> navController.navigate("options")

                    }
                }
            )
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

       composable("options"){

           OptionsScreen()
       }

    }
}

/**
 *
 *
 *
 * if the focus is on the note body or title while categories are active, close categories - done
 *
 *
 * confirm category deletion - feels unecessary
 *
 * set up repository
 *
 * in settings screen/ maybe call it options instead of settings - done
 *
 * restore tutorial note - need to know what all to include in tutorial first, for prepopulating https://developer.android.com/training/data-storage/room/prepopulate
 * disable categories
 * export all notes
 * import txt file
 * delete all notes
 * delete all categories
 * select font size
 * turn off reminders
 * dark mode
 *
 * single note settings
 *
 * reminder
 * share note
 * create email with note (through share)
 * search
 * export
 * backup
 *
 *
 */
