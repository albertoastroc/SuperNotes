package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.NoteCategory
import com.gmail.pentominto.us.supernotes.data.SavedNote
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import com.gmail.pentominto.us.supernotes.utility.DateGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditScreenViewModel @Inject constructor(
    private val repository : LocalRepository,
    savedStateHandle : SavedStateHandle
) : ViewModel() {

    private val noteId : Int = checkNotNull(savedStateHandle["noteId"])

    private val _noteEditState : MutableState<NoteEditState> = mutableStateOf(NoteEditState())
    val noteEditState : State<NoteEditState> = _noteEditState

    private fun getNote(noteId : Int) {

        if (noteId == 0) {

            insertNewNote()
        } else {

            viewModelScope.launch {

                repository.getNoteWithCategory(noteId).collect { categoryNoteMap ->

                    categoryNoteMap.forEach {

                        _noteEditState.value = _noteEditState.value.copy(
                            noteCategory = it.key,
                            noteTitle = it.value.noteTitle,
                            noteBody = it.value.noteBody
                        )
                    }
                }
            }
        }
    }

    private fun insertNewNote() {

        viewModelScope.launch {

            getNote(
                repository.insertNote(
                    SavedNote(
                        category = "No Category",
                        createdDate = DateGetter.getCurrentDate(),
                        lastModified = DateGetter.getCurrentDate(),
                        noteBody = noteEditState.value.noteBody,
                        noteTitle = noteEditState.value.noteTitle
                    )
                ).toInt()
            )
        }
    }

    private fun getCategories() {

        viewModelScope.launch {

            repository.getAllCategories().collect {

                _noteEditState.value = _noteEditState.value.copy(categories = it)
            }
        }
    }

    private fun getCurrentDate() {

        _noteEditState.value = _noteEditState.value.copy(
            currentDate = DateGetter.getCurrentDate()
        )
    }

    fun insertCategory(categoryName : String) {

        viewModelScope.launch {

            saveNoteText()
            repository.insertCategory(NoteCategory(categoryName))
        }
    }

    fun deleteCategory(category : NoteCategory) {

        viewModelScope.launch {

            val notesToUpdate = repository.getNotesOfThisCategory(category.categoryTitle)

            repository.deleteCategory(category.categoryId)

            notesToUpdate.collect { notesList ->

                notesList.forEach { note ->

                    repository.updateNoteCategory(
                        "No NoteCategory",
                        note.noteId
                    )
                }
            }
        }
    }

    fun saveNoteText() {

        viewModelScope.launch {
            repository.updateNote(
                noteTitle = _noteEditState.value.noteTitle,
                noteBody = _noteEditState.value.noteBody,
                noteId = noteId,
                lastModified = _noteEditState.value.currentDate
            )
        }
    }

    fun saveNoteCategory(category : NoteCategory) {

        viewModelScope.launch {

            saveNoteText()
            repository.updateNoteCategory(
                chosenCategory = category.categoryTitle,
                noteId = noteId
            )
        }
    }

    fun onTitleInputChange(newInput : String) {

        _noteEditState.value = _noteEditState.value.copy(
            noteTitle = newInput
        )
    }

    fun onBodyInputChange(newInput : String) {

        _noteEditState.value = _noteEditState.value.copy(
            noteBody = newInput
        )
    }

    init {
        getNote(noteId)
        getCategories()
        getCurrentDate()
    }
}