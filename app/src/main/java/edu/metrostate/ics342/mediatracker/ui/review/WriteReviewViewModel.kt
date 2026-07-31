package edu.metrostate.ics342.mediatracker.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.network.CreateReviewRequest
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class WriteReviewViewModel : ViewModel() {

    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating.asStateFlow()

    private val _reviewText = MutableStateFlow("")
    val reviewText: StateFlow<String> = _reviewText.asStateFlow()

    private val _shareToFeed = MutableStateFlow(true)
    val shareToFeed: StateFlow<Boolean> = _shareToFeed.asStateFlow()

    sealed class SubmitState {
        data object Idle : SubmitState()
        data object Loading : SubmitState()
        data object Success : SubmitState()
        data class Error(val message: String) : SubmitState()
    }

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState.asStateFlow()

    fun onRatingChange(value: Int) {
        _rating.value = value
        resetSubmitState()
    }

    fun onReviewTextChange(value: String) {
        _reviewText.value = value
        resetSubmitState()
    }

    fun onShareToFeedChange(value: Boolean) {
        _shareToFeed.value = value
        resetSubmitState()
    }

    fun submitReview(mediaId: Int) {
        if (_rating.value == 0) {
            _submitState.value = SubmitState.Error("Please select a rating")
            return
        }

        if (_reviewText.value.length > 500) {
            _submitState.value = SubmitState.Error("Review text must be 500 characters or less")
            return
        }

        viewModelScope.launch {
            _submitState.value = SubmitState.Loading

            try {
                val request = CreateReviewRequest(
                    mediaId = mediaId,
                    rating = _rating.value,
                    reviewText = _reviewText.value.ifBlank { null },
                    shareToFeed = if (_shareToFeed.value) true else null
                )

                val response = RetrofitInstance.reviewApiService.createReview(request)

                when (response.code()) {
                    200, 201 -> {
                        _submitState.value = SubmitState.Success
                    }
                    409 -> {
                        _submitState.value = SubmitState.Error("You've already reviewed this item")
                    }
                    else -> {
                        val errorBody = response.errorBody()?.string()
                        _submitState.value = SubmitState.Error(
                            errorBody?.let { "Server error: $it" } ?: "Failed to submit review (${response.code()})"
                        )
                    }
                }
            } catch (e: IOException) {
                _submitState.value = SubmitState.Error("Network error: ${e.message}")
            } catch (e: Exception) {
                _submitState.value = SubmitState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    private fun resetSubmitState() {
        if (_submitState.value !is SubmitState.Loading) {
            _submitState.value = SubmitState.Idle
        }
    }
}