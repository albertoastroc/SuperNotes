package com.gmail.pentominto.us.supernotes.noteeditscreen

import com.gmail.pentominto.us.supernotes.data.Note

data class NoteEditScreenState(
    val noteTitle : Note = Note(),
    val bottomDrawerExpanded : Boolean = false
)
