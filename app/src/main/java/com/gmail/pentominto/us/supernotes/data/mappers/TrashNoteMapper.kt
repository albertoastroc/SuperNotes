package com.gmail.pentominto.us.supernotes.data.mappers

import com.gmail.pentominto.us.supernotes.data.entities.NoteEntity
import com.gmail.pentominto.us.supernotes.data.entities.TrashNoteEntity
import com.gmail.pentominto.us.supernotes.data.models.Note
import com.gmail.pentominto.us.supernotes.data.models.TrashNote

fun TrashNoteEntity.toTrashNote() : TrashNote {
    return TrashNote(
        noteTitle = noteTitle,
        noteBody = noteBody,
        createdDate = createdDate,
        lastModified = lastModified,
        dateDeleted = dateDeleted
    )
}

fun TrashNote.toTrashNoteEntity() : TrashNoteEntity {
    return TrashNoteEntity(
        noteTitle = noteTitle,
        noteBody = noteBody,
        createdDate = createdDate,
        lastModified = lastModified,
        dateDeleted = dateDeleted
    )
}