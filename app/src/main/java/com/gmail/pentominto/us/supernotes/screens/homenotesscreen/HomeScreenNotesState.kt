package com.gmail.pentominto.us.supernotes.screens.homenotesscreen

import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note

data class HomeScreenNotesState(
    val notes: Map<Category, List<Note>> = emptyMap(),
    val searchResults: List<Note> = emptyList(),
    val searchBarInput: String = "",
    val showCategories: Boolean = true,
    val trashEnabled: Boolean = true
)
