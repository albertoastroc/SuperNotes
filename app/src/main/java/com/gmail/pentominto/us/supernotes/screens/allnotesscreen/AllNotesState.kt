package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import com.gmail.pentominto.us.supernotes.data.NoteCategory
import com.gmail.pentominto.us.supernotes.data.SavedNote

data class AllNotesState(
    val notes : Map<NoteCategory, List<SavedNote>> = emptyMap(),
    val notesSearchResults : List<SavedNote> = emptyList(),
    val searchBarInput : String = "",
    val showCategories : Boolean = true,
    val trashEnabled : Boolean = true,
    val currentList : CurrentList = CurrentList.WITH_CATEGORIES
)

enum class CurrentList {

    WITHOUT_CATEGORIES,
    WITH_CATEGORIES

}