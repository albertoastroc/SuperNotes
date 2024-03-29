package com.gmail.pentominto.us.supernotes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(

    var noteTitle: String = "",

    var noteBody: String = "",

    var category: String = "No Category",

    var createdDate: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_db_id")
    var noteId: Int = 0
)
