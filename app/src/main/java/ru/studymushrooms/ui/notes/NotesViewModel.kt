package ru.studymushrooms.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.studymushrooms.App
import ru.studymushrooms.api.NoteModel
import ru.studymushrooms.repository.NoteRepository
import ru.studymushrooms.utils.SingleLiveEvent
import java.util.*

class NotesViewModel(
    private val noteRepository: NoteRepository,
) : ViewModel() {
    private val _showErrorToastEvents = SingleLiveEvent<String>()
    val showErrorToastEvents: LiveData<String> = _showErrorToastEvents

    private val _notes: MutableLiveData<List<NoteModel>> = MutableLiveData()
    val notes: LiveData<List<NoteModel>> = _notes

    fun loadData() {
        if (notes.value == null) {
            viewModelScope.launch {
                try {
                    val notes = withContext(Dispatchers.IO) {
                        noteRepository.getNotes(App.token!!)
                    }
                    _notes.value = notes
                } catch (t: Throwable) {
                    _showErrorToastEvents.value = t.message
                }
            }
        }
    }

    fun saveNote(title: String, content: String) {
        viewModelScope.launch {
            try {
                noteRepository.postNote(App.token!!, title, content, Date())
            } catch (t: Throwable) {
                // TODO: do something
            }
        }
    }

    fun updateNote(title: String, newContent: String) { // todo протестить
        viewModelScope.launch {
            try {
                noteRepository.updateNote(App.token!!, title, newContent, Date())
            } catch (t: Throwable) {
                // TODO: c'mon, do something
            }
        }
    }

    fun deleteNote(title: String, content: String) { // todo протестить
        viewModelScope.launch {
            try {
                noteRepository.deleteNote(App.token!!, title, content)
            } catch (e: Exception) {
                // TODO: ыыыыыыыыыыыыыыыыыыыыыыыы
            }
        }
    }
}
