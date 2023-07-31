package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.activities.mainactivity.MainActivity
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import com.gmail.pentominto.us.supernotes.utility.AlarmReceiver
import com.gmail.pentominto.us.supernotes.utility.Constants.DEFAULT_CATEGORY
import com.gmail.pentominto.us.supernotes.utility.DateGetter.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditScreenViewModel @Inject constructor(
    private val repository : LocalRepository,
    savedStateHandle : SavedStateHandle
) : ViewModel() {

    private var noteId : Int = checkNotNull(savedStateHandle["noteid"])

    private val _noteEditState : MutableState<NoteEditState> = mutableStateOf(NoteEditState())
    val noteEditState : State<NoteEditState> = _noteEditState

    private fun getNote(noteId : Int) {

        viewModelScope.launch {

            repository.getNoteWithCategory(noteId).collect { categoryNoteMap ->

                categoryNoteMap.forEach {
                    _noteEditState.value = _noteEditState.value.copy(
                        noteTitle = it.value.noteTitle,
                        noteBody = it.value.noteBody,
                        currentChosenCategory = it.value.category

                    )
                }
            }
        }
    }

    fun insertNewNote() {
        viewModelScope.launch {
            noteId = repository.insertNote(
                Note(
                    category = DEFAULT_CATEGORY,
                    createdDate = getCurrentDate()
                )
            ).toInt()
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect {
                _noteEditState.value = _noteEditState.value.copy(categories = it)
            }
        }
    }

    fun insertCategory(categoryName : String) {
        viewModelScope.launch {
            repository.insertCategory(Category(categoryName))
        }
    }

    fun deleteCategory(category : Category) {
        viewModelScope.launch {

            val notesToUpdate = repository.getNotesOfThisCategory(category.categoryTitle)

            repository.deleteCategory(category.categoryId)

            notesToUpdate.collect { notesList ->

                notesList.forEach { note ->

                    repository.updateNoteCategory(
                        DEFAULT_CATEGORY,
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
                noteId = noteId
            )
        }
    }

    fun saveNoteCategory(category : Category) {
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

    fun setAlarm(context : Context, alarmTime : Long) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)

        alarmIntent.putExtra("note_id", noteId)
        alarmIntent.putExtra("note_title", noteEditState.value.noteTitle)
        alarmIntent.putExtra("note_body", noteEditState.value.noteBody)

        val broadcastPendingIntent = PendingIntent.getBroadcast(context,
            noteId,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val mainActivityIntent = Intent(context,
            MainActivity::class.java
        )
        val activityPendingIntent = PendingIntent.getActivity(context,
            noteId,
            mainActivityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val clockInfo = AlarmManager.AlarmClockInfo(alarmTime, activityPendingIntent)

        alarmManager.setAlarmClock(clockInfo, broadcastPendingIntent)
    }

    init {

        if (noteId == 0) {

            insertNewNote()
        }

        getNote(noteId)
        getCategories()
        getCurrentDate()
    }
}
