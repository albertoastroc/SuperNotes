package com.gmail.pentominto.us.supernotes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trash_note_table")
data class DiscardedNote(

    var noteTitle: String,

    var noteBody: String,

    var createdDate: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trash_note_db_id")
    var noteId: Int = 0
)
