package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note

data class AllNotesState(
    val notesWithCategory : Map<Category, List<Note>> = emptyMap(),
    val notesWithNoCategories : List<Note> = emptyList(),
    val notesSearchResults : List<Note> = emptyList(),
    val categories : List<Category> = emptyList(),
    val searchBarInput : String = String(),
    val showCategories : Boolean = true,
    val trashEnabled : Boolean = true,
    val currentList : CurrentList = CurrentList.WITH_CATEGORIES
)

enum class CurrentList {

    WITHOUT_CATEGORIES,
    WITH_CATEGORIES
}