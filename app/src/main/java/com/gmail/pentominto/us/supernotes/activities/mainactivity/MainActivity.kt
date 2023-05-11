@file:OptIn(ExperimentalAnimationApi::class)

package com.gmail.pentominto.us.supernotes.activities.mainactivity

import android.os.Bundle
import android.os.Process
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import com.gmail.pentominto.us.supernotes.MainActivityViewModel
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables.AllNotesScreen
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables.NoteEditScreen
import com.gmail.pentominto.us.supernotes.screens.optionsscreen.composables.OptionsScreen
import com.gmail.pentominto.us.supernotes.screens.readonlynotescreen.composables.ReadOnlyNoteScreen
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.composables.TrashNotesScreen
import com.gmail.pentominto.us.supernotes.ui.theme.MostlyBlackBlue
import com.gmail.pentominto.us.supernotes.ui.theme.SuperNotesTheme
import com.gmail.pentominto.us.supernotes.utility.Constants.DEFAULT_ANIMATION_DURATION
import com.gmail.pentominto.us.supernotes.utility.Constants.NOTE_ID
import com.gmail.pentominto.us.supernotes.utility.Constants.TRASH_NOTE_ID
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()

        ViewModelProvider(this).get(MainActivityViewModel::class.java)

        Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT)

        setContent {
            SuperNotesTheme(
                darkTheme = isSystemInDarkTheme()
            ) {
                val isSystemDarkTheme = isSystemInDarkTheme()
                val systemUiController = rememberSystemUiController()
                LaunchedEffect(
                    key1 = isSystemDarkTheme
                ) {
                    if (isSystemDarkTheme) {
                        systemUiController.setSystemBarsColor(
                            color = MostlyBlackBlue,
                            darkIcons = false
                        )
                    } else {
                        systemUiController.setSystemBarsColor(
                            color = Color.White,
                            darkIcons = true
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

    MyNavHost(
        navController
    )
}

@Composable
private fun MyNavHost(navController: NavHostController) {
    val context = LocalContext.current

    AnimatedNavHost(
        navController = navController,
        startDestination = NavigationId.ALL_NOTES.destination
    ) {
        navigationWithTransition(
            routeName = NavigationId.ALL_NOTES.destination,
            destinations = listOf(
                NavigationId.EDIT_NOTE.destination + "/{$NOTE_ID}",
                NavigationId.OPTIONS.destination,
                NavigationId.ALL_TRASH_NOTES.destination
            ),
            arguments = emptyList()
        ) {
            AllNotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate("${NavigationId.EDIT_NOTE.destination}/$noteId")
                },
                onOptionsClick = { menuItemId ->

                    when (menuItemId) {
                        OptionMenuId.OPTIONS.optionMenuId -> navController.navigate(NavigationId.OPTIONS.destination)
                        OptionMenuId.TRASH.optionMenuId     -> navController.navigate(NavigationId.ALL_TRASH_NOTES.destination)
                        OptionMenuId.PLAY_STORE.optionMenuId     -> context.startActivity(NavIntents.getPlaystoreIntent())
                        OptionMenuId.PRIVACY_POLICY.optionMenuId -> context.startActivity(NavIntents.getPrivacyPolicyIntent())
                    }
                }
            )
        }

        navigationWithTransition(
            routeName = NavigationId.ALL_TRASH_NOTES.destination,
            destinations = listOf(
                NavigationId.ALL_NOTES.destination,
                NavigationId.TRASH_NOTE.destination + "/{$TRASH_NOTE_ID}"
            ),
            arguments = emptyList()
        ) {
            TrashNotesScreen(
                onTrashNoteClick = { trashNoteId ->
                    navController.navigate(NavigationId.TRASH_NOTE.destination + "/$trashNoteId")
                }
            )
        }

        navigationWithTransition(
            routeName = NavigationId.EDIT_NOTE.destination + "/{$NOTE_ID}",
            destinations = listOf(NavigationId.ALL_NOTES.destination),
            arguments = listOf(
                navArgument(NOTE_ID) { type = NavType.IntType }
            )
        ) {
            val noteId = remember {
                it.arguments?.getInt(NOTE_ID)
            }

            if (noteId != null) {
                NoteEditScreen(noteId = noteId)
            }
        }

        navigationWithTransition(
            routeName = NavigationId.TRASH_NOTE.destination + "/{$TRASH_NOTE_ID}",
            destinations = listOf(NavigationId.ALL_TRASH_NOTES.destination),
            arguments = listOf(
                navArgument(TRASH_NOTE_ID) { type = NavType.IntType }
            )
        ) {
            val trashNoteId = remember {
                it.arguments?.getInt(TRASH_NOTE_ID)
            }
            if (trashNoteId != null) {
                ReadOnlyNoteScreen(trashNoteId = trashNoteId)
            }
        }

        navigationWithTransition(
            routeName = NavigationId.OPTIONS.destination,
            destinations = listOf(NavigationId.ALL_NOTES.destination),
            arguments = emptyList()
        ) {
            OptionsScreen()
        }
    }
}

fun NavGraphBuilder.navigationWithTransition(
    routeName: String,
    destinations: List<String>,
    arguments: List<NamedNavArgument>,
    content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        routeName,
        enterTransition = {
            when (initialState.destination.route) {
                in destinations ->

                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
                    )
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                in destinations ->
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
                    )
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                in destinations ->
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
                    )
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                in destinations ->
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
                    )
                else -> null
            }
        },
        content = content,
        arguments = arguments
    )
}
