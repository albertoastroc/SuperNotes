package com.gmail.pentominto.us.supernotes.utility

object Constants {

    // DB
    const val DATABASE_NAME = "notes_database"

    // DataStore
    const val PREFERENCES_STORE_NAME = "user_preferences"

    const val FIREBASE_ID_KEY = "user_id"

    const val USER_TRASH_ENABLED_KEY = "trash_enabled"
    const val USER_HIDE_CATEGORIES_KEY = "hide_categories"

    // MainActivity
    const val DEFAULT_ANIMATION_DURATION = 500

    const val NOTE_EDIT_NAVIGATION_URI = "myapp://supernotes/noteeditscreen/{noteid}"

    const val NOTE_ID = "noteid"
    const val TRASH_NOTE_ID = "trashNoteId"

    const val TRASH_NOTE_CATEGORY_TAG = "TrashNotesAPPTAG"

    // Composables

    // SwipeableTrashNoteRow

    const val CURRENT_POSITION = .99f
    const val FRACTION_THRESHOLD = .6f

    // AllNotes

    const val DEFAULT_CATEGORY = "No Category"

}
