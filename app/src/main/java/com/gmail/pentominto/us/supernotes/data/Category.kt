package com.gmail.pentominto.us.supernotes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(

    var categoryTitle : String = "No Category",

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_db_id")
    var categoryId : Int = 0
)
