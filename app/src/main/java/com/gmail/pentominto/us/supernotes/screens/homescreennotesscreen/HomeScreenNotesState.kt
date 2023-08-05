package com.gmail.pentominto.us.supernotes.screens.homescreennotesscreen

import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note

data class HomeScreenNotesState(
    val notes: Map<Category, List<Note>> = emptyMap(),
    val notesSearchResults: List<Note> = emptyList(),
    val searchBarInput: String = "",
    val showCategoryTitles: Boolean = true,
    val trashEnabled: Boolean = true
)
