package com.gmail.pentominto.us.supernotes.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note

data class CategoryWIthNotes(

    @Embedded val category : Category,
    @Relation(
        parentColumn = "category_db_id",
        entityColumn = "note_db_id",
        associateBy = Junction(CategoryNoteCrossRef::class)
    )

    val notes : List<Note>

)
