@file:OptIn(ExperimentalAnimationApi::class)

package com.gmail.pentominto.us.supernotes.activities.mainactivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.NavDrawerId
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.NavIntents
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.NavigationId
import com.gmail.pentominto.us.supernotes.screens.homenotesscreen.composables.HomeNotesScreen
import com.gmail.pentominto.us.supernotes.screens.noteeditscreen.composables.NoteEditScreen
import com.gmail.pentominto.us.supernotes.screens.optionsscreen.composables.OptionsScreen
import com.gmail.pentominto.us.supernotes.screens.readonlynotescreen.composables.ReadOnlyNoteScreen
import com.gmail.pentominto.us.supernotes.screens.trashnotescreen.composables.TrashNotesScreen
import com.gmail.pentominto.us.supernotes.ui.theme.SuperNotesTheme
import com.gmail.pentominto.us.supernotes.utility.Constants.NOTE_EDIT_NAVIGATION_URI
import com.gmail.pentominto.us.supernotes.utility.Constants.NOTE_ID
import com.gmail.pentominto.us.supernotes.utility.Constants.TRASH_NOTE_ID
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel(this)

        actionBar?.hide()

        ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setContent {
            SuperNotesTheme {
                val systemUiController = rememberSystemUiController()
                val systemBarColor = MaterialTheme.colors.background
                val isSystemDarkTheme = isSystemInDarkTheme()

                LaunchedEffect(
                    key1 = isSystemDarkTheme
                ) {
                    if (isSystemDarkTheme) {
                        systemUiController.setSystemBarsColor(
                            color = systemBarColor,
                            darkIcons = false
                        )
                    } else {
                        systemUiController.setSystemBarsColor(
                            color = systemBarColor,
                            darkIcons = true
                        )
                    }
                }
                SuperNotesApp(navController = rememberNavController())
            }
        }
    }
}

@Composable
private fun SuperNotesApp(navController : NavHostController) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NavigationId.ALL_NOTES.destination
    ) {
        composable(
            route = NavigationId.ALL_NOTES.destination,
            arguments = emptyList(),
            deepLinks = emptyList()
        ) {
            HomeNotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate("${NavigationId.EDIT_NOTE.destination}/$noteId")
                },
                onDrawerItemClick = { menuItemId ->

                    navigateToMenuItem(menuItemId,
                        navController,
                        context
                    )
                }
            )
        }

        composable(
            route = NavigationId.ALL_TRASH_NOTES.destination,
            arguments = emptyList(),
            deepLinks = emptyList()
        ) {

            TrashNotesScreen(
                onTrashNoteClick = { trashNoteId ->
                    navController.navigate(NavigationId.TRASH_NOTE.destination + "/$trashNoteId")

                },
                onDrawerItemClick = { menuItemId ->

                    navigateToMenuItem(menuItemId,
                        navController,
                        context
                    )
                }
            )
        }

        /**
         *
         * Deeplinks used for navigating from a reminder notification
         *
         */

        composable(
            route = NavigationId.EDIT_NOTE.destination + "/{$NOTE_ID}",
            arguments = listOf(
                navArgument(NOTE_ID) { type = NavType.IntType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = NOTE_EDIT_NAVIGATION_URI
                    action = Intent.ACTION_VIEW
                }
            )
        ) {
            val noteId = remember {
                it.arguments?.getInt(NOTE_ID)
            }

            if (noteId != null) {
                NoteEditScreen(
                    noteid = noteId,
                    onDrawerItemClick = { menuItemId ->

                        navigateToMenuItem(menuItemId,
                            navController,
                            context
                        )
                    }

                )
            }
        }

        composable(
            route = NavigationId.TRASH_NOTE.destination + "/{$TRASH_NOTE_ID}",
            arguments = listOf(
                navArgument(TRASH_NOTE_ID) { type = NavType.IntType }
            ),
            deepLinks = emptyList()
        ) {
            val trashNoteId = remember {
                it.arguments?.getInt(TRASH_NOTE_ID)
            }
            if (trashNoteId != null) {
                ReadOnlyNoteScreen(trashNoteId = trashNoteId,
                    onDrawerItemClick = { menuItemId ->

                        navigateToMenuItem(menuItemId,
                            navController,
                            context
                        )
                    }
                )
            }
        }

        composable(
            route = NavigationId.OPTIONS.destination,
            arguments = emptyList(),
            deepLinks = emptyList()
        ) {
            OptionsScreen()
        }
    }
}

/**
 *
 * Helps with navigation when triggered from Nav Drawer
 *
 */
private fun navigateToMenuItem(menuItemId : Int, navController : NavHostController, context : Context) {
    when (menuItemId) {

        NavDrawerId.OPTIONS.navDrawerId        -> navController.navigate(
            NavigationId.OPTIONS.destination
        )

        NavDrawerId.TRASH.navDrawerId          -> navController.navigate(
            NavigationId.ALL_TRASH_NOTES.destination
        )

        NavDrawerId.PLAY_STORE.navDrawerId     -> context.startActivity(
            NavIntents.getPlaystoreIntent()
        )

        NavDrawerId.PRIVACY_POLICY.navDrawerId -> context.startActivity(
            NavIntents.getPrivacyPolicyIntent()
        )
    }
}

private fun createNotificationChannel(activity : Context) {
    val channelName = "Reminders"
    val channelDescriptionText = "Allows note reminder notifications."
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(
        "1",
        channelName,
        importance
    ).apply {
        description = channelDescriptionText
    }
    val notificationManager : NotificationManager =
        activity.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
