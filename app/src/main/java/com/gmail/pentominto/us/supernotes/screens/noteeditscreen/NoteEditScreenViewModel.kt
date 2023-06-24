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
    private val repository: LocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var noteId: Int = checkNotNull(savedStateHandle["noteid"])

    private val _noteEditState: MutableState<NoteEditState> = mutableStateOf(NoteEditState())
    val noteEditState: State<NoteEditState> = _noteEditState

    private val _note: MutableState<Note> = mutableStateOf(Note(createdDate = getCurrentDate(), date = null))

    private fun getNote(noteId: Int) {

        viewModelScope.launch {

            repository.getNoteWithCategory(noteId).collect { categoryNoteMap ->


                categoryNoteMap.forEach {
                    _noteEditState.value = _noteEditState.value.copy(
                        noteTitle = it.value.noteTitle,
                        noteBody = it.value.noteBody,
                        noteCategory = it.value.category

                    )

                    _note.value = _note.value.copy(
                        noteId = it.value.noteId,
                        noteTitle = it.value.noteTitle,
                        noteBody = it.value.noteBody,
                        category = it.value.category,
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
                    createdDate = getCurrentDate(),
                    date = null
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

    fun insertCategory(categoryName: String) {
        viewModelScope.launch {
            saveNoteText()
            repository.insertCategory(Category(categoryName))
        }
    }

    fun deleteCategory(category: Category) {
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

    fun saveNoteCategory(category: Category) {
        viewModelScope.launch {
            saveNoteText()
            repository.updateNoteCategory(
                chosenCategory = category.categoryTitle,
                noteId = noteId
            )
        }
    }

    fun onTitleInputChange(newInput: String) {
        _noteEditState.value = _noteEditState.value.copy(
            noteTitle = newInput
        )
    }

    fun setAlarm(context : Context, testLong : Long) {
        // creating alarmManager instance
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // adding intent and pending intent to go to AlarmReceiver Class in future
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("task_info", _note.value)
        val pendingIntent = PendingIntent.getBroadcast(context, _note.value.noteId, intent, PendingIntent.FLAG_IMMUTABLE)
        // when using setAlarmClock() it displays a notification until alarm rings and when pressed it takes us to mainActivity
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        val basicPendingIntent = PendingIntent.getActivity(context, _note.value.noteId, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
        // creating clockInfo instance
        val clockInfo =  AlarmManager.AlarmClockInfo(testLong, basicPendingIntent)
        // setting the alarm
        alarmManager.setAlarmClock(clockInfo, pendingIntent)
    }

    fun onBodyInputChange(newInput: String) {
        _noteEditState.value = _noteEditState.value.copy(
            noteBody = newInput
        )
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
