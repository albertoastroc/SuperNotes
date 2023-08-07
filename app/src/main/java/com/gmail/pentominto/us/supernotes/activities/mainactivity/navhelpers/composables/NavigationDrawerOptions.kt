package com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.composables

import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.DrawerMenuOptions
import com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers.MenuItem
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerOptions(
    drawerState: DrawerState,
    onDrawerItemClick: (Int) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    NavigationDrawerContainer(
        drawerOptionsList = listOf(
            MenuItem(
                id = DrawerMenuOptions.OPTIONS.menuId,
                title = DrawerMenuOptions.OPTIONS.title,
                icon = DrawerMenuOptions.OPTIONS.icon
            ),
            MenuItem(
                id = DrawerMenuOptions.TRASH.menuId,
                title = DrawerMenuOptions.TRASH.title,
                icon = DrawerMenuOptions.TRASH.icon
            ),
            MenuItem(
                id = DrawerMenuOptions.RATE_ME.menuId,
                title = DrawerMenuOptions.RATE_ME.title,
                icon = DrawerMenuOptions.RATE_ME.icon
            ),
            MenuItem(
                id = DrawerMenuOptions.PRIVACY_POLICY.menuId,
                title = DrawerMenuOptions.PRIVACY_POLICY.title,
                icon = DrawerMenuOptions.PRIVACY_POLICY.icon
            )
        ),
        onSettingClick = {
            coroutineScope.launch {
                drawerState.close()
                onDrawerItemClick(it)
            }
        }
    )
}
