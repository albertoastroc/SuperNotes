package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import com.gmail.pentominto.us.supernotes.data.Note

data class NoteEditScreenState(
    val noteTitle : Note = Note(),
    val bottomDrawerExpanded : Boolean = false
)
