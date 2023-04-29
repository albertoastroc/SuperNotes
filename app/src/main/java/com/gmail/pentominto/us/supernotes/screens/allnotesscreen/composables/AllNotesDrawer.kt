package com.gmail.pentominto.us.supernotes.screens.allnotesscreen.composables

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.DrawerMenuOptions
import com.gmail.pentominto.us.supernotes.screens.allnotesscreen.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AllNotesDrawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    onOptionsClick: (Int) -> Unit
) {
    Drawer(
        drawerOptionsList = listOf(
            MenuItem(
                id = DrawerMenuOptions.HOME.menuId,
                title = DrawerMenuOptions.HOME.title,
                icon = DrawerMenuOptions.HOME.icon
            ),
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
            scope.launch {
                scaffoldState.drawerState.close()
                onOptionsClick(it)
            }
        }
    )
}
