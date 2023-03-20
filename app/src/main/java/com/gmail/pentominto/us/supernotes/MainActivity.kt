@file:OptIn(ExperimentalAnimationApi::class)

package com.gmail.pentominto.us.supernotes

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.*
import com.gmail.pentominto.us.supernotes.screens.TrashNotesScreen
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables.AllNotesScreen
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditScreen
import com.gmail.pentominto.us.supernotes.screens.optionsscreen.composables.OptionsScreen
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.ReadOnlyNoteScreen
import com.gmail.pentominto.us.supernotes.ui.theme.Spider
import com.gmail.pentominto.us.supernotes.ui.theme.SuperNotesTheme
import com.gmail.pentominto.us.supernotes.utility.Constants.DEFAULT_ANIMATION_DURATION
import com.gmail.pentominto.us.supernotes.utility.NavIntentsGetter
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

val userDarkThemeKey = booleanPreferencesKey("user_theme")
val userIdKey = stringPreferencesKey("user_id")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : MainActivityViewModel by viewModels()

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()

        viewModel.setUpFirebaseId()

        viewModel.isDarkThemePreferred()

        val isDarkTheme : MutableState<Boolean> =  viewModel.isDarkThemeState

        setContent {

            SuperNotesTheme(
                darkTheme = isDarkTheme.value
            ) {
                val systemUiController = rememberSystemUiController()
                LaunchedEffect(
                    key1 = isDarkTheme.value
                ) {
                    if (!isDarkTheme.value) {
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
    val context = LocalContext.current

    val navController = rememberAnimatedNavController()

    MyNavHost(
        navController,
        context
    )
}

@Composable
private fun MyNavHost(navController : NavHostController, context : Context) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "allNotes"
    ) {
        navigationWithTransition(
            routeName = "allNotes",
            destinations = listOf(
                "noteEdit/{noteId}",
                "options",
                "trash"
            ),
            arguments = emptyList()
        ) {
            AllNotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate("noteEdit/$noteId")
                },
                onOptionsClick = { menuItemId ->

                    when (menuItemId) {
                        2 -> navController.navigate("options")
                        3 -> navController.navigate("trash")
                        4 -> context.startActivity(NavIntentsGetter.getPlaystoreIntent())
                        5 -> context.startActivity(NavIntentsGetter.getPrivacyPolicyIntent())
                    }
                }
            )
        }

        navigationWithTransition(
            routeName = "trash",
            destinations = listOf(
                "allNotes",
                "readOnlyNote/{trashNoteId}"
            ),
            arguments = emptyList()
        ) {
            TrashNotesScreen(
                onTrashNoteClick = { trashNoteId ->
                    navController.navigate("readOnlyNote/$trashNoteId")
                }
            )
        }

        navigationWithTransition(
            routeName = "noteEdit/{noteId}",
            destinations = listOf("allNotes"),
            arguments = listOf(
                navArgument("noteId") { type = NavType.IntType }
            )
        ) {
            val noteId = remember {
                it.arguments?.getInt("noteId")
            }

            if (noteId != null) {
                NoteEditScreen(noteId = noteId)
            }
        }

        navigationWithTransition(
            routeName = "readOnlyNote/{trashNoteId}",
            destinations = listOf("trash"),
            arguments = listOf(
                navArgument("trashNoteId") { type = NavType.IntType }
            )
        ) {
            val trashNoteId = remember {
                it.arguments?.getInt("trashNoteId")
            }
            if (trashNoteId != null) {
                ReadOnlyNoteScreen(trashNoteId = trashNoteId)
            }
        }

        navigationWithTransition(
            routeName = "options",
            destinations = listOf("allNotes"),
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
