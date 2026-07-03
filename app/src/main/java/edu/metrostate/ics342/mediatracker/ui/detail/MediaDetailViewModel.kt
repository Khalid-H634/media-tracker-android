package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailViewModel : ViewModel() {


    sealed class MediaDetailUiState {
        data object Loading : MediaDetailUiState()
        data class Success(val media: Media, val reviews: List<Review> = emptyList()) : MediaDetailUiState()
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


            val media = FakeMediaRepository.mediaList.find { it.id == mediaId }

            if (media != null) {

                val reviews = getFakeReviews(mediaId)
                _uiState.value = MediaDetailUiState.Success(media, reviews)
            } else {
                _uiState.value = MediaDetailUiState.Error("Media not found")
            }
        }
    }

    private fun getFakeReviews(mediaId: Int): List<Review> {
        return listOf(
            Review(
                userId = "user-001",
                mediaId = mediaId,
                rating = 5,
                reviewText = "Absolutely loved this! The writing was incredible and the plot kept me on the edge of my seat. Highly recommend to anyone looking for their next favorite.",
                createdAt = "2024-01-15T10:30:00Z",
                user = FakeMediaRepository.currentUser
            ),
            Review(
                userId = "user-002",
                mediaId = mediaId,
                rating = 4,
                reviewText = "Really enjoyed this, though the pacing felt a bit slow in the middle. Overall a fantastic experience with a satisfying conclusion.",
                createdAt = "2024-01-18T14:20:00Z",
                user = FakeMediaRepository.followers.firstOrNull()
            ),
            Review(
                userId = "user-003",
                mediaId = mediaId,
                rating = 3,
                reviewText = "Good but not great. Had high expectations and while it delivered on some fronts, it fell short in others. Still worth a look.",
                createdAt = "2024-01-20T09:15:00Z",
                user = FakeMediaRepository.following.firstOrNull()
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