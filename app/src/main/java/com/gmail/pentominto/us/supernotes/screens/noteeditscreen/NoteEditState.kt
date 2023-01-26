package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import com.gmail.pentominto.us.supernotes.data.entities.CategoryEntity
import com.gmail.pentominto.us.supernotes.data.entities.NoteEntity
import com.gmail.pentominto.us.supernotes.data.models.Note


data class NoteEditState(
    val note : Note = Note(),
    val noteTitle : String = String(),
    val noteBody : String = String(),
    val categories : List<CategoryEntity> = emptyList(),
    val noteCategory : CategoryEntity = CategoryEntity(),
    val currentDate : String = String()
)
