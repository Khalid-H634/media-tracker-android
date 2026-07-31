package edu.metrostate.ics342.mediatracker.ui.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.ui.components.StarRatingRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    viewModel: WriteReviewViewModel = viewModel()
) {
    val rating by viewModel.rating.collectAsState()
    val reviewText by viewModel.reviewText.collectAsState()
    val submitState by viewModel.submitState.collectAsState()

    val isLoading = submitState is WriteReviewViewModel.SubmitState.Loading
    val isSuccess = submitState is WriteReviewViewModel.SubmitState.Success
    val errorMsg = (submitState as? WriteReviewViewModel.SubmitState.Error)?.message

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.write_review_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("Cancel")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Rating",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            StarRatingRow(
                rating = rating,
                onRatingChange = viewModel::onRatingChange,
                starSize = 40
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (rating == 0) {
                    "Tap a star to rate"
                } else {
                    "You selected $rating stars"
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (rating == 0) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = reviewText,
                onValueChange = viewModel::onReviewTextChange,
                label = { Text("Review (optional)") },
                placeholder = { Text("What did you think?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 8,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                supportingText = {
                    Text(
                        text = "${reviewText.length}/500",
                        color = if (reviewText.length > 500) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                },
                isError = reviewText.length > 500
            )

            Spacer(Modifier.height(16.dp))

            if (errorMsg != null) {
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.submitReview(mediaId) },
                enabled = rating > 0 && reviewText.length <= 500 && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Post Review")
                }
            }
        }
    }
}