package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.MediaDetail
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.network.AddFavoriteRequest
import edu.metrostate.ics342.mediatracker.data.network.AddToLibraryRequest
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailViewModel : ViewModel() {

    sealed class MediaDetailUiState {
        data object Loading : MediaDetailUiState()
        data class Success(
            val media: MediaDetail,
            val reviews: List<Review> = emptyList(),
            val isInLibrary: Boolean = false,
            val libraryStatus: LibraryStatus? = null,
            val isFavorited: Boolean = false
        ) : MediaDetailUiState()
        data class Error(val message: String) : MediaDetailUiState()
    }

    private val _uiState = MutableStateFlow<MediaDetailUiState>(MediaDetailUiState.Loading)
    val uiState: StateFlow<MediaDetailUiState> = _uiState.asStateFlow()

    private val _mediaId = MutableStateFlow(-1)
    val mediaId: StateFlow<Int> = _mediaId.asStateFlow()

    private val _isAddingToLibrary = MutableStateFlow(false)
    val isAddingToLibrary: StateFlow<Boolean> = _isAddingToLibrary.asStateFlow()

    private val _isFavoriting = MutableStateFlow(false)
    val isFavoriting: StateFlow<Boolean> = _isFavoriting.asStateFlow()

    fun setMediaId(id: Int) {
        _mediaId.value = id
        loadMediaDetail(id)
    }

    fun loadMediaDetail(mediaId: Int) {
        viewModelScope.launch {
            _uiState.value = MediaDetailUiState.Loading

            try {
                val media = FakeMediaRepository.sampleMediaDetail

                var isInLibrary = false
                var libraryStatus: LibraryStatus? = null
                try {
                    val response = RetrofitInstance.libraryApiService.getLibraryItem(mediaId)
                    if (response.isSuccessful) {
                        response.body()?.let { itemResponse ->
                            isInLibrary = true
                            libraryStatus = LibraryStatus.fromString(itemResponse.status)
                        }
                    }
                } catch (e: Exception) {
                    // 404 means not in library
                }

                var isFavorited = false
                try {
                    val response = RetrofitInstance.favoritesApiService.getFavorite(mediaId)
                    if (response.isSuccessful) {
                        isFavorited = true
                    }
                } catch (e: Exception) {
                    // 404 means not favorited
                }

                _uiState.value = MediaDetailUiState.Success(
                    media = media,
                    reviews = emptyList(),
                    isInLibrary = isInLibrary,
                    libraryStatus = libraryStatus,
                    isFavorited = isFavorited
                )

            } catch (e: Exception) {
                _uiState.value = MediaDetailUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun addToLibrary() {
        viewModelScope.launch {
            _isAddingToLibrary.value = true
            try {
                val request = AddToLibraryRequest(
                    mediaId = _mediaId.value,
                    status = "want_to"
                )
                val response = RetrofitInstance.libraryApiService.addToLibrary(request)

                if (response.isSuccessful) {
                    val currentState = _uiState.value
                    if (currentState is MediaDetailUiState.Success) {
                        _uiState.value = currentState.copy(
                            isInLibrary = true,
                            libraryStatus = LibraryStatus.WANT_TO
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isAddingToLibrary.value = false
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _isFavoriting.value = true
            try {
                val currentState = _uiState.value
                if (currentState is MediaDetailUiState.Success) {
                    if (currentState.isFavorited) {
                        val response = RetrofitInstance.favoritesApiService.removeFavorite(_mediaId.value)
                        if (response.isSuccessful || response.code() == 404) {
                            _uiState.value = currentState.copy(isFavorited = false)
                        }
                    } else {
                        val request = AddFavoriteRequest(_mediaId.value)
                        val response = RetrofitInstance.favoritesApiService.addFavorite(request)

                        if (response.isSuccessful || response.code() == 409) {
                            _uiState.value = currentState.copy(isFavorited = true)
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isFavoriting.value = false
            }
        }
    }

    fun refresh() {
        val currentId = _mediaId.value
        if (currentId != -1) {
            loadMediaDetail(currentId)
        }
    }
}