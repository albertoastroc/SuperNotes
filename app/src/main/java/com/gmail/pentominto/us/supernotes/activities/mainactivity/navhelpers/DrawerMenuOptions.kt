package com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class DrawerMenuOptions(val menuId: Int, val title: String, val icon: ImageVector) {

    OPTIONS(
        2,
        "Options",
        Icons.Default.Settings
    ),
    TRASH(
        3,
        "Trash",
        Icons.Default.Delete
    ),
    RATE_ME(
        4,
        "Rate me in the Play Store!",
        Icons.Default.Star
    ),
    PRIVACY_POLICY(
        5,
        "Privacy policy and info",
        Icons.Default.Info
    )
}