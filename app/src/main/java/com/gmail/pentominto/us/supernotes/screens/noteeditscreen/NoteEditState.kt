package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note

data class NoteEditState(
    val note: Note? = null,
    val noteTitle: String = "",
    val noteBody: String = "",
    val categories: List<Category> = emptyList(),
    val currentDate: String = ""
)
