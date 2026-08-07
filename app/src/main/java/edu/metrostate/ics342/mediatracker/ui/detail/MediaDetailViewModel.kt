package edu.metrostate.ics342.mediatracker.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.SessionRepository
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
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

class MediaDetailViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val sessionRepo: SessionRepository = DefaultSessionRepository(application)

    sealed class MediaDetailUiState {
        data object Loading : MediaDetailUiState()
        data class Success(
            val media: MediaDetail,
            val reviews: List<Review> = emptyList(),
            val ownReview: Review? = null,
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

    private val _deleteDialogState = MutableStateFlow<Int?>(null)
    val deleteDialogState: StateFlow<Int?> = _deleteDialogState.asStateFlow()

    private var currentUserId: String? = null

    init {
        viewModelScope.launch {
            try {
                currentUserId = sessionRepo.getUser()?.id
            } catch (e: Exception) {
                currentUserId = null
            }
        }
    }

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
                    val libResponse = RetrofitInstance.libraryApiService.getLibraryItem(mediaId)
                    if (libResponse.isSuccessful) {
                        libResponse.body()?.let {
                            isInLibrary = true
                            libraryStatus = LibraryStatus.fromString(it.status)
                        }
                    }
                } catch (_: Exception) { }

                var isFavorited = false
                try {
                    val favResponse = RetrofitInstance.favoritesApiService.getFavorite(mediaId)
                    if (favResponse.isSuccessful) isFavorited = true
                } catch (_: Exception) { }

                val reviewsResponse = RetrofitInstance.reviewApiService.getReviews(mediaId)
                val allReviews = if (reviewsResponse.isSuccessful) {
                    reviewsResponse.body() ?: emptyList()
                } else {
                    emptyList()
                }

                val ownReview = currentUserId?.let { userId ->
                    allReviews.find { it.userId == userId }
                }

                _uiState.value = MediaDetailUiState.Success(
                    media = mediaDetail,
                    reviews = allReviews,
                    ownReview = ownReview,
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
                val request = AddToLibraryRequest(_mediaId.value, "want_to")
                val response = RetrofitInstance.libraryApiService.addToLibrary(request)
                if (response.isSuccessful) {
                    val current = _uiState.value
                    if (current is MediaDetailUiState.Success) {
                        _uiState.value = current.copy(
                            isInLibrary = true,
                            libraryStatus = LibraryStatus.WANT_TO
                        )
                    }
                }
            } catch (_: Exception) { } finally {
                _isAddingToLibrary.value = false
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _isFavoriting.value = true
            try {
                val current = _uiState.value
                if (current is MediaDetailUiState.Success) {
                    if (current.isFavorited) {
                        val response = RetrofitInstance.favoritesApiService.removeFavorite(_mediaId.value)
                        if (response.isSuccessful || response.code() == 404) {
                            _uiState.value = current.copy(isFavorited = false)
                        }
                    } else {
                        val request = AddFavoriteRequest(_mediaId.value)
                        val response = RetrofitInstance.favoritesApiService.addFavorite(request)
                        if (response.isSuccessful || response.code() == 409) {
                            _uiState.value = current.copy(isFavorited = true)
                        }
                    }
                }
            } catch (_: Exception) { } finally {
                _isFavoriting.value = false
            }
        }
    }

    fun showDeleteDialog(reviewId: Int) {
        _deleteDialogState.value = reviewId
    }

    fun dismissDeleteDialog() {
        _deleteDialogState.value = null
    }

    fun confirmDelete() {
        val reviewId = _deleteDialogState.value ?: return
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.reviewApiService.deleteReview(reviewId)
                if (response.isSuccessful) {
                    val current = _uiState.value
                    if (current is MediaDetailUiState.Success) {
                        val newReviews = current.reviews.filter { it.id != reviewId }
                        _uiState.value = current.copy(
                            reviews = newReviews,
                            ownReview = if (current.ownReview?.id == reviewId) null else current.ownReview
                        )
                    }
                }
            } catch (_: Exception) { } finally {
                dismissDeleteDialog()
            }
        }
    }

    fun refresh() {
        val id = _mediaId.value
        if (id != -1) loadMediaDetail(id)
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MediaDetailViewModel(application) as T
                }
            }
        }
    }
}