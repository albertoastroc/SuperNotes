package com.gmail.pentominto.us.supernotes.data.mappers

import com.gmail.pentominto.us.supernotes.data.entities.NoteEntity
import com.gmail.pentominto.us.supernotes.data.models.Note

fun NoteEntity.toNote() : Note {
    return Note(
        noteTitle = noteTitle,
        noteBody = noteBody,
        category = category,
        createdDate = createdDate,
        lastModified = lastModified
    )
}

fun Note.toNoteEntity() : NoteEntity {
    return NoteEntity(
        noteTitle = noteTitle,
        noteBody = noteBody,
        category = category,
        createdDate = createdDate,
        lastModified = lastModified
    )
}