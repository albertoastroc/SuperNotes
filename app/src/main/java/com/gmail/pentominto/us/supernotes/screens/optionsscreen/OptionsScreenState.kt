package com.gmail.pentominto.us.supernotes.screens.optionsscreen

data class OptionsScreenState(
    val categoriesOption : Boolean = true,
    val darkThemeOption : Boolean = false,
    val trashEnabled : Boolean = true,
    val userId : String = "",
    val currentDate : String = ""
)
