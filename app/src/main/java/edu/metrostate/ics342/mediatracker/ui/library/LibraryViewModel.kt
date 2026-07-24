package edu.metrostate.ics342.mediatracker.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.network.LibraryItemResponse
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import edu.metrostate.ics342.mediatracker.data.network.toLibraryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {

    private val _libraryItems = MutableStateFlow<List<LibraryItem>>(emptyList())
    val libraryItems: StateFlow<List<LibraryItem>> = _libraryItems.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadLibrary()
    }

    fun loadLibrary() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = RetrofitInstance.libraryApiService.getLibrary()

                if (response.isSuccessful) {
                    val items: List<LibraryItemResponse> = response.body() ?: emptyList()
                    _libraryItems.value = items.map { it.toLibraryItem() }
                } else {
                    _errorMessage.value = "Failed to load library: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeItem(mediaId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.libraryApiService.removeFromLibrary(mediaId)
                if (response.isSuccessful) {
                    _libraryItems.value = _libraryItems.value.filter { it.mediaId != mediaId }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun updateStatus(mediaId: Int, newStatus: LibraryStatus) {
        _libraryItems.value = _libraryItems.value.map { item ->
            if (item.mediaId == mediaId) {
                item.copy(status = newStatus)
            } else {
                item
            }
        }
    }

    fun refresh() {
        loadLibrary()
    }
}