package com.gmail.pentominto.us.supernotes.activities.mainactivity.navhelpers

enum class NavigationId(
    val destination: String
) {

    ALL_NOTES(
        destination = "allNotes"
    ),
    OPTIONS(
        destination = "options"
    ),
    EDIT_NOTE(
        destination = "noteEdit"
    ),
    ALL_TRASH_NOTES(
        destination = "trash"
    ),
    TRASH_NOTE(
        destination = "trashNote"
    )
}
