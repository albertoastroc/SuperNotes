package com.gmail.pentominto.us.supernotes.screens.readonlynotescreen

import com.gmail.pentominto.us.supernotes.data.DiscardedNote

data class ReadOnlyNoteScreenState(
    val trashNote : DiscardedNote? = null,
)
