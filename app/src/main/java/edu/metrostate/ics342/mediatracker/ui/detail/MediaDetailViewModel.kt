package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

                val mediaResponse = RetrofitInstance.mediaApiService.getMediaById(mediaId)

                if (!mediaResponse.isSuccessful) {
                    _uiState.value = MediaDetailUiState.Error("Failed to load media: ${mediaResponse.code()}")
                    return@launch
                }

                val media = mediaResponse.body()
                if (media == null) {
                    _uiState.value = MediaDetailUiState.Error("Media not found")
                    return@launch
                }


                val mediaDetail = MediaDetail(
                    id = media.id,
                    mediaType = media.mediaType,
                    title = media.title,
                    author = media.author,
                    director = media.director,
                    creator = media.creator,
                    network = media.network,
                    coverUrl = media.coverUrl,
                    publishedYear = media.publishedYear,
                    averageRating = media.averageRating,
                    ratingCount = media.ratingCount,
                    genres = media.genres,
                    description = null,
                    pageCount = null,
                    runtimeMinutes = null,
                    seasonCount = null,
                    episodeCount = null,
                    isbn = null,
                    reviewCount = 0
                )


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

                }


                var isFavorited = false
                try {
                    val response = RetrofitInstance.favoritesApiService.getFavorite(mediaId)
                    if (response.isSuccessful) {
                        isFavorited = true
                    }
                } catch (e: Exception) {

                }


                val reviewsResponse = RetrofitInstance.reviewApiService.getReviews(mediaId)
                android.util.Log.d("REVIEWS", "Code: ${reviewsResponse.code()}")
                android.util.Log.d("REVIEWS", "Body: ${reviewsResponse.body()}")

                val reviews = if (reviewsResponse.isSuccessful) {
                    reviewsResponse.body() ?: emptyList()
                } else {
                    emptyList()
                }
                android.util.Log.d("REVIEWS", "Count: ${reviews.size}")

                _uiState.value = MediaDetailUiState.Success(
                    media = mediaDetail,
                    reviews = reviews,
                    isInLibrary = isInLibrary,
                    libraryStatus = libraryStatus,
                    isFavorited = isFavorited
                )

            } catch (e: Exception) {
                android.util.Log.e("MEDIA_DETAIL", "Error: ${e.message}", e)
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