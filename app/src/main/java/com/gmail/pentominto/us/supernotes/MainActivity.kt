@file:OptIn(ExperimentalAnimationApi::class)

package com.gmail.pentominto.us.supernotes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.gmail.pentominto.us.supernotes.screens.TrashNotesScreen
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.AllNotesScreen
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.NoteEditScreen
import com.gmail.pentominto.us.supernotes.screens.optionsscreen.OptionsScreen
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.ReadOnlyNoteScreen
import com.gmail.pentominto.us.supernotes.ui.theme.Spider
import com.gmail.pentominto.us.supernotes.ui.theme.SuperNotesTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

val userDarkThemeKey = booleanPreferencesKey("user_theme")
val userIdKey = stringPreferencesKey("user_id")

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

        // sets up firebase id
        lifecycleScope.launch {

            dataStore.edit { preferences ->

                if (! preferences.contains(userIdKey)) {

                    val userId = UUID.randomUUID().toString()
                    preferences[userIdKey] = userId
                }
            }

            dataStore.data.collect { preferences ->

                Firebase.crashlytics.setUserId(preferences[userIdKey] ?: String())
                Firebase.analytics.setUserId(preferences[userIdKey] ?: String())
            }
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

    val context = LocalContext.current

    AnimatedNavHost(
        navController = navController,
        startDestination = "allNotes",

        ) {
        composable(
            "allNotes",
            enterTransition = {
                when (initialState.destination.route) {
                    "noteEdit/{noteId}", "options", "trash" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else                                    -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "noteEdit/{noteId}", "options", "trash" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else                                    -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "noteEdit/{noteId}", "options", "trash" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else                                    -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "noteEdit/{noteId}", "options", "trash" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else                                    -> null
                }
            }
        ) {

            val emailIntent = Intent()
                .setData(Uri.parse("mailto:simplenotesacf@gmail.com"))
                .setAction(Intent.ACTION_SENDTO)
                .putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Simple notes feedback"
                )

            val playstoreIntent = Intent()
                .setData(Uri.parse("market://details?id=packagename"))
                .setAction(Intent.ACTION_VIEW)
                .addFlags(
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )

            AllNotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate("noteEdit/${noteId}")
                },
                onOptionsClick = { menuItemId ->

                    when (menuItemId) {

                        2 -> navController.navigate("options")
                        3 -> navController.navigate("trash")
                        4 -> context.startActivity(emailIntent)
                        5 -> {
                            TODO("playstore intent")
                        }

                    }
                }
            )
        }

        composable("trash",
            enterTransition = {
                when (initialState.destination.route) {
                    "allNotes", "readOnlyNote/{trashNoteId}" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else                                     -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "allNotes", "readOnlyNote/{trashNoteId}" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else                                     -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "allNotes", "readOnlyNote/{trashNoteId}" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else                                     -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "allNotes", "readOnlyNote/{trashNoteId}" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else                                     -> null
                }
            }) {

            TrashNotesScreen(
                onTrashNoteClick = { trashNoteId ->
                    navController.navigate("readOnlyNote/${trashNoteId}")
                }
            )
        }

        composable(
            "noteEdit/{noteId}",
            enterTransition = {
                when (initialState.destination.route) {
                    "allNotes" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else       -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "allNotes" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else       -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "allNotes" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else       -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "allNotes" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else       -> null
                }
            },
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

        composable(
            "readOnlyNote/{trashNoteId}",
            enterTransition = {
                when (initialState.destination.route) {
                    "trash" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else    -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "trash" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else    -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "trash" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else    -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "trash" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else    -> null
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

        composable("options",
            enterTransition = {
                when (initialState.destination.route) {
                    "allNotes" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else       -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "allNotes" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else       -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "allNotes" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else       -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "allNotes" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else       -> null
                }
            }
        ) {

            OptionsScreen()
        }
    }
}
