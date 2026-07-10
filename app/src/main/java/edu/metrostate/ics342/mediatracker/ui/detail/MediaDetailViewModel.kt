package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.MediaDetail
import edu.metrostate.ics342.mediatracker.data.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailViewModel : ViewModel() {

    sealed class MediaDetailUiState {
        data object Loading : MediaDetailUiState()
        data class Success(val media: MediaDetail, val reviews: List<Review> = emptyList()) : MediaDetailUiState()
        data class Error(val message: String) : MediaDetailUiState()
    }

    private val _uiState = MutableStateFlow<MediaDetailUiState>(MediaDetailUiState.Loading)
    val uiState: StateFlow<MediaDetailUiState> = _uiState.asStateFlow()

    private val _mediaId = MutableStateFlow(-1)
    val mediaId: StateFlow<Int> = _mediaId.asStateFlow()

    fun setMediaId(id: Int) {
        _mediaId.value = id
        loadMediaDetail(id)
    }

    fun loadMediaDetail(mediaId: Int) {
        viewModelScope.launch {
            _uiState.value = MediaDetailUiState.Loading

            // Using fake data - this will be replaced with API calls in Week 8
            val media = FakeMediaRepository.sampleMediaDetail

            if (media.id == mediaId || mediaId == 1080) {
                val reviews = getFakeReviews(mediaId)
                _uiState.value = MediaDetailUiState.Success(media, reviews)
            } else {
                // Try to find in mediaList
                val found = FakeMediaRepository.mediaList.find { it.id == mediaId }
                if (found != null) {
                    val detail = MediaDetail(
                        id = found.id,
                        mediaType = found.mediaType,
                        title = found.title,
                        author = found.author,
                        director = found.director,
                        creator = found.creator,
                        network = found.network,
                        coverUrl = found.coverUrl,
                        publishedYear = found.publishedYear,
                        averageRating = found.averageRating,
                        ratingCount = found.ratingCount,
                        genres = found.genres,
                        description = "No description available.",
                        reviewCount = 0
                    )
                    _uiState.value = MediaDetailUiState.Success(detail, emptyList())
                } else {
                    _uiState.value = MediaDetailUiState.Error("Media not found")
                }
            }
        }
    }

    private fun getFakeReviews(mediaId: Int): List<Review> {
        return listOf(
            Review(
                userId = "user-001",
                mediaId = mediaId,
                rating = 5,
                reviewText = "Absolutely loved this!",
                createdAt = "2024-01-15T10:30:00Z",
                user = FakeMediaRepository.currentUser
            ),
            Review(
                userId = "user-002",
                mediaId = mediaId,
                rating = 4,
                reviewText = "Really enjoyed this.",
                createdAt = "2024-01-18T14:20:00Z",
                user = FakeMediaRepository.followers.firstOrNull()
            )
        )
    }

    fun refresh() {
        val currentId = _mediaId.value
        if (currentId != -1) {
            loadMediaDetail(currentId)
        }
    }
}