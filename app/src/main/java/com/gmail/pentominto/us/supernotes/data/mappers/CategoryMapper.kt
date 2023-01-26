package com.gmail.pentominto.us.supernotes.data.mappers

import com.gmail.pentominto.us.supernotes.data.entities.CategoryEntity
import com.gmail.pentominto.us.supernotes.data.entities.NoteEntity
import com.gmail.pentominto.us.supernotes.data.models.Category
import com.gmail.pentominto.us.supernotes.data.models.Note

fun CategoryEntity.toCategory() : Category {
    return Category(
        categoryTitle = categoryTitle
    )
}

fun Category.toCategoryEntity() : CategoryEntity {
    return CategoryEntity(
        categoryTitle = categoryTitle
    )
}