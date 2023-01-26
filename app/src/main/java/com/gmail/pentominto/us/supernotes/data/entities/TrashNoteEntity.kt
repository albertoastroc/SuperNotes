package com.gmail.pentominto.us.supernotes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trash_note_table")
data class TrashNoteEntity(

    var noteTitle : String,

    var noteBody : String,

    var createdDate : String,

    var lastModified : String,

    var dateDeleted : String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trash_note_db_id")
    var noteId : Long = 0,
)