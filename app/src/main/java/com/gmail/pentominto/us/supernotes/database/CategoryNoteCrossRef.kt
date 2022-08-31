package com.gmail.pentominto.us.supernotes.database

import androidx.room.Entity


@Entity(
    tableName = "category_note_cross_ref",
    primaryKeys = ["category_db_id", "note_db_id"]
)
data class CategoryNoteCrossRef(

    val category_db_id : Int,
    val note_db_id : Int,

)