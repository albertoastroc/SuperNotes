@file:OptIn(ExperimentalAnimationApi::class)

package com.gmail.pentominto.us.supernotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.gmail.pentominto.us.supernotes.screens.TrashNotesScreen
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.AllNotesScreen
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditScreen
import com.gmail.pentominto.us.supernotes.screens.optionsscreen.OptionsScreen
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.ReadOnlyNoteScreen
import com.gmail.pentominto.us.supernotes.ui.theme.Spider
import com.gmail.pentominto.us.supernotes.ui.theme.SuperNotesTheme
import com.gmail.pentominto.us.supernotes.utility.Constants.ALL_NOTES
import com.gmail.pentominto.us.supernotes.utility.Constants.NOTE_ID
import com.gmail.pentominto.us.supernotes.utility.Constants.OPTIONS
import com.gmail.pentominto.us.supernotes.utility.Constants.TRASH
import com.gmail.pentominto.us.supernotes.utility.Constants.USER_THEME_KEY
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val userDarkThemeKey = booleanPreferencesKey(USER_THEME_KEY)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore : DataStore<Preferences>

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        val themeFlow : Flow<Boolean> = dataStore.data
            .map { preferences ->
                preferences[userDarkThemeKey] ?: false
            }

        setContent {

            val savedTheme : MutableState<Boolean> = runBlocking { mutableStateOf(themeFlow.first()) }

            val latestTheme = themeFlow.collectAsState(initial = false)

            val themeState : MutableState<Boolean> = remember(key1 = latestTheme.value) { mutableStateOf(savedTheme.value) }

            SuperNotesTheme(
                darkTheme = themeState.value
            ) {

                val systemUiController = rememberSystemUiController()
                SideEffect {

                    if (! themeState.value) {

                        systemUiController.setSystemBarsColor(
                            color = Color.White,
                            darkIcons = true
                        )
                    } else {

                        systemUiController.setSystemBarsColor(
                            color = Spider,
                            darkIcons = false
                        )
                    }
                }
                SuperNotesApp()
            }
        }
    }
}

@Composable
fun SuperNotesApp() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = ALL_NOTES,

        ) {
        composable(
            ALL_NOTES,
            enterTransition = {
                when (initialState.destination.route) {
                    "noteEdit/{$NOTE_ID}", OPTIONS, TRASH ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else                                  -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "noteEdit/{$NOTE_ID}", OPTIONS, TRASH ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else                                  -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "noteEdit/{$NOTE_ID}", OPTIONS, TRASH ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else                                  -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "noteEdit/{$NOTE_ID}", OPTIONS, TRASH ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else                                  -> null
                }
            }
        ) {

            AllNotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate("noteEdit/${NOTE_ID}")
                },
                onOptionsClick = { menuItemId ->

                    when (menuItemId) {

                        2 -> navController.navigate(OPTIONS)
                        3 -> navController.navigate(TRASH)

                    }
                }
            )
        }

        composable(
            TRASH,
            enterTransition = {
                when (initialState.destination.route) {
                    ALL_NOTES, "readOnlyNote/{trashNoteId}" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else                                    -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    ALL_NOTES, "readOnlyNote/{trashNoteId}" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else                                    -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    ALL_NOTES, "readOnlyNote/{trashNoteId}" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else                                    -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    ALL_NOTES, "readOnlyNote/{trashNoteId}" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else                                    -> null
                }
            }) {

            TrashNotesScreen(
                onTrashNoteClick = { trashNoteId ->
                    navController.navigate("readOnlyNote/${trashNoteId}")
                }
            )
        }

        composable(
            "noteEdit/{$NOTE_ID}",
            enterTransition = {
                when (initialState.destination.route) {
                    ALL_NOTES ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else      -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    ALL_NOTES ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else      -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    ALL_NOTES ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else      -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    ALL_NOTES ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else      -> null
                }
            },
            arguments = listOf(
                navArgument(NOTE_ID) {
                    type = NavType.LongType
                }
            ),
        ) {

            val noteId = remember {
                it.arguments?.getLong(NOTE_ID)
            }
            if (noteId != null) {
                NoteEditScreen(noteId = noteId)
            }
        }

        composable(
            "readOnlyNote/{trashNoteId}",
            enterTransition = {
                when (initialState.destination.route) {
                    TRASH ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else  -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    TRASH ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else  -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    TRASH ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else  -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    TRASH ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else  -> null
                }
            },
            arguments = listOf(
                navArgument("trashNoteId") {
                    type = NavType.LongType
                }
            )
        ) {

            val trashNoteId = remember {
                it.arguments?.getLong("trashNoteId")
            }
            if (trashNoteId != null) {
                ReadOnlyNoteScreen(trashNoteId = trashNoteId)
            }
        }

        composable(
            OPTIONS,
            enterTransition = {
                when (initialState.destination.route) {
                    ALL_NOTES ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else      -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    ALL_NOTES ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else      -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    ALL_NOTES ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else      -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    ALL_NOTES ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else      -> null
                }
            }
        ) {

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
 * need focus to come up to line being edited
 *
 * in settings screen/ maybe call it options instead of settings - done
 *
 * restore tutorial note - need to know what all to include in tutorial first, for prepopulating https://developer.android.com/training/data-storage/room/prepopulate
 * disable categories - done
 * export all notes - for v2
 * import txt file - for v2
 * delete all notes - done
 * search notes - done
 *
 * dark mode - done
 *
 * single note settings
 *
 * copy to clipboard - done
 * reminder - wait until calendar api is easy to use
 * share note - done
 * waiting on google bug fix to put category on bottom of screen, bug makes screen not function in landscape mode
 * text not being saved in orientation change - fixed
 * new note is being saved on orientation change if its a new note, no problem with existing notes - fixed
 * dialogbackground looks wrong, text on light container on dark - fixed
 * find a way to implement screen for tablets - fixed
 * make fab dissapear when scrolling - fixed
 * screen transition animations
 *
 *
 *
 * ORGANIZATION
 * repository - not done

 * string resources - not done
 * image resources - not done
 */