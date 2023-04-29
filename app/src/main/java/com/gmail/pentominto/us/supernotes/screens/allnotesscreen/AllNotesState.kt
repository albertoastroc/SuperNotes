package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note

data class AllNotesState(
    val notes : Map<Category, List<Note>> = emptyMap(),
    val notesSearchResults : List<Note> = emptyList(),
    val searchBarInput : String = "",
    val showCategoryTitles : Boolean = true,
    val trashEnabled : Boolean = true
)
