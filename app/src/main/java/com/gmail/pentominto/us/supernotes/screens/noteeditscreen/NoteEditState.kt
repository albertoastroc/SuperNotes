package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import com.gmail.pentominto.us.supernotes.data.Category

data class NoteEditState(
    val noteTitle : String = "",
    val noteBody : String = "",
    val currentChosenCategory : String = "",
    val categories : List<Category> = emptyList()
)
