package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note


data class NoteEditState(
    val note : Note = Note(),
    val noteTitle : String? = String(),
    val noteBody : String? = String(),
    val categories : List<Category> = emptyList(),
    val noteCategory : Category = Category(),
    val currentDate : String = String()
)
