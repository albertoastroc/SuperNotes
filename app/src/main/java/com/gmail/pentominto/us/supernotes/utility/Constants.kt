package com.gmail.pentominto.us.supernotes.utility

object Constants {

    // DB
    const val DATABASE_NAME = "notes_database"
    const val PREPOP_DATABASE_NAME = "notes_database.db"

    // DataStore
    const val PREFERENCES_STORE_NAME = "user_preferences"

    const val FIREBASE_ID_KEY = "user_id"

    const val USER_THEME_KEY = "user_theme"
    const val USER_TRASH_ENABLED_KEY = "trash_enabled"
    const val USER_HIDE_CATEGORIES_KEY = "hide_categories"

    // MainActivity
    const val DEFAULT_ANIMATION_DURATION = 700

    const val ALL_NOTES_DESTINATION = "allNotes"

    const val NOTE_EDIT_DESTINATION = "noteEdit"
    const val NOTE_EDIT_NOTE_EDIT_DESTINATION = "noteEdit/{noteId}"
    const val NOTE_ID = "noteId"

    const val OPTIONS_DESTINATION = "options"

    const val TRASH_DESTINATION = "trash"
    const val TRASH_NOTE = "trashNote"
    const val TRASH_NOTE_ID = "trashNoteId"

    const val TRASH_NOTE_CATEGORY_TAG = "TrashNotesAPPTAG"

    // Composables

    //SwipeableTrashNoteRow

    const val CURRENT_POSITION = .99f
    const val FRACTION_THRESHOLD = .6f

    // AllNotes

    const val DEFAULT_CATEGORY = "No Category"

    const val ABOUT_THIS_APP_TITLE = "About this app"

    const val ABOUT_THIS_APP_NOTE = "Thanks for installing the app.  This note includes some basic info and works as a mini FAQ.\n" +
        "\n" +
        "1.  If you have a suggestion for a feature you would like to see you can mention it in your review or send an email to simplenotesacf@gmail.com.\n" +
        "\n" +
        "2. There is no save button, the app auto-saves everything.\n" +
        "\n" +
        "3. To delete a note, swipe it to the right, to restore a note from the Trash, swipe it to the left.\n" +
        "\n" +
        "4. To add and set a category use the menu at the top right of this screen. \n" +
        "\n" +
        "5. If this note is deleted, it can later be restored in Options.\n" +
        "\n"
}
