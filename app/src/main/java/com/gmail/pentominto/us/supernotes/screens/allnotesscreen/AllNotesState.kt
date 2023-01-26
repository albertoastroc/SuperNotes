package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import com.gmail.pentominto.us.supernotes.data.entities.CategoryEntity
import com.gmail.pentominto.us.supernotes.data.entities.NoteEntity

data class AllNotesState(
    val notesWithCategory : Map<CategoryEntity, List<NoteEntity>> = emptyMap(),
    val notesWithNoCategories : List<NoteEntity> = emptyList(),
    val notesSearchResults : List<NoteEntity> = emptyList(),
    val categories : List<CategoryEntity> = emptyList(),
    val searchBarInput : String = String(),
    val showCategories : Boolean = true,
    val trashEnabled : Boolean = true,
    val currentDate : String = String(),
    val currentList : CurrentList = CurrentList.WITH_CATEGORIES
)

enum class CurrentList {

    WITHOUT_CATEGORIES,
    WITH_CATEGORIES
}