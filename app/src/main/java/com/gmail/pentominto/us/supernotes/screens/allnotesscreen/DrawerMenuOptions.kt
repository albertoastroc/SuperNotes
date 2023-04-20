package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class DrawerMenuOptions(val menuId: Int, val title: String, val icon: ImageVector) {

    HOME(1, "Home", Icons.Default.Home),
    OPTIONS(2, "Options", Icons.Default.Settings),
    TRASH(3, "Trash", Icons.Default.Delete),
    RATE_ME(4, "Rate me in the Play Store!", Icons.Default.Star),
    PRIVACY_POLICY(5, "Privacy policy and info", Icons.Default.Info)
}