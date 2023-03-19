package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import com.gmail.pentominto.us.supernotes.data.NoteCategory
import com.gmail.pentominto.us.supernotes.data.SavedNote


data class NoteEditState(
    val note : SavedNote? = null,
    val noteTitle : String = "",
    val noteBody : String = "",
    val categories : List<NoteCategory> = emptyList(),
    val noteCategory : NoteCategory = NoteCategory(),
    val currentDate : String = ""
)
