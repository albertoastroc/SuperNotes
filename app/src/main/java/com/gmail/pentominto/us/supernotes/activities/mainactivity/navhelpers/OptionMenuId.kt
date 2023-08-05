package com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers

enum class OptionMenuId(
    val optionName: String,
    val optionMenuId: Int
) {

    HOME(
        optionName = "Home",
        optionMenuId = 1
    ),
    OPTIONS(
        optionName = "Options",
        optionMenuId = 2
    ),
    TRASH(
        optionName = "Trash",
        optionMenuId = 3
    ),
    PLAY_STORE(
        optionName = "Play Store",
        optionMenuId = 4
    ),
    PRIVACY_POLICY(
        optionName = "Privacy Policy",
        optionMenuId = 5
    )
}
