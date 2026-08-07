package edu.metrostate.ics342.mediatracker.ui.detail

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.MediaDetail
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.model.creatorCredit
import edu.metrostate.ics342.mediatracker.theme.MovieContainer
import edu.metrostate.ics342.mediatracker.theme.OnMovieContainer
import edu.metrostate.ics342.mediatracker.ui.components.StatusBadge
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    onWriteReview: (Int) -> Unit,
    onEditReview: (Int, Review) -> Unit,
    viewModel: MediaDetailViewModel = viewModel(
        factory = MediaDetailViewModel.provideFactory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val isAddingToLibrary by viewModel.isAddingToLibrary.collectAsState()
    val isFavoriting by viewModel.isFavoriting.collectAsState()
    val deleteDialogState by viewModel.deleteDialogState.collectAsState()

    LaunchedEffect(mediaId) {
        viewModel.setMediaId(mediaId)
    }

    if (deleteDialogState != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = { Text("Delete Review") },
            text = { Text("Are you sure you want to delete your review?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.confirmDelete()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "More")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is MediaDetailViewModel.MediaDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is MediaDetailViewModel.MediaDetailUiState.Error -> {
                Column(
                    Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${state.message}")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.refresh() }) {
                        Text("Retry")
                    }
                }
            }

            is MediaDetailViewModel.MediaDetailUiState.Success -> {
                MediaDetailContent(
                    detail = state.media,
                    reviews = state.reviews,
                    ownReview = state.ownReview,
                    isInLibrary = state.isInLibrary,
                    libraryStatus = state.libraryStatus,
                    isFavorited = state.isFavorited,
                    isAddingToLibrary = isAddingToLibrary,
                    isFavoriting = isFavoriting,
                    onAddToLibrary = { viewModel.addToLibrary() },
                    onToggleFavorite = { viewModel.toggleFavorite() },
                    onWriteReview = { onWriteReview(mediaId) },
                    onEditReview = { review -> onEditReview(mediaId, review) },
                    onDeleteReview = { reviewId -> viewModel.showDeleteDialog(reviewId) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun MediaDetailContent(
    detail: MediaDetail,
    reviews: List<Review>,
    ownReview: Review?,
    isInLibrary: Boolean,
    libraryStatus: LibraryStatus?,
    isFavorited: Boolean,
    isAddingToLibrary: Boolean,
    isFavoriting: Boolean,
    onAddToLibrary: () -> Unit,
    onToggleFavorite: () -> Unit,
    onWriteReview: () -> Unit,
    onEditReview: (Review) -> Unit,
    onDeleteReview: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MediaCover(detail)
            Spacer(Modifier.height(14.dp))
            Text(detail.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(detail.creatorCredit(LocalContext.current), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (isInLibrary && libraryStatus != null) {
                Spacer(Modifier.height(6.dp))
                StatusBadge(status = libraryStatus)
            }
            Spacer(Modifier.height(8.dp))
            RatingSummary(detail.averageRating, detail.ratingCount)
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onAddToLibrary,
                modifier = Modifier.weight(1f),
                enabled = !isAddingToLibrary
            ) {
                if (isAddingToLibrary) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(
                        if (isInLibrary) {
                            when (libraryStatus) {
                                LibraryStatus.WANT_TO -> "✓ Want To"
                                LibraryStatus.IN_PROGRESS -> "In Progress"
                                LibraryStatus.FINISHED -> "Finished"
                                else -> "In Library"
                            }
                        } else {
                            stringResource(R.string.detail_add_want_to)
                        }
                    )
                }
            }

            OutlinedButton(
                onClick = onToggleFavorite,
                modifier = Modifier.weight(1f),
                enabled = !isFavoriting
            ) {
                if (isFavoriting) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.primary)
                } else {
                    Icon(
                        if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (isFavorited) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(if (isFavorited) "Saved" else stringResource(R.string.detail_save))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (!detail.description.isNullOrBlank()) {
            SectionCaption(stringResource(R.string.detail_about))
            Spacer(Modifier.height(6.dp))
            Text(detail.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(20.dp))
        }

        StatGrid(detail)
        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionCaption(
                text = stringResource(R.string.detail_reviews_count, detail.reviewCount),
                modifier = Modifier.weight(1f)
            )
            if (ownReview == null) {
                TextButton(onClick = onWriteReview) {
                    Text(stringResource(R.string.detail_write_review))
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        when {
            reviews.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Be the first to review this.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (ownReview == null) {
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = onWriteReview) {
                                Text("Write a Review")
                            }
                        }
                    }
                }
            }
            else -> {
                val sortedReviews = if (ownReview != null) {
                    listOf(ownReview) + reviews.filter { it.id != ownReview.id }
                } else {
                    reviews
                }

                sortedReviews.forEach { review ->
                    val isOwn = review.id == ownReview?.id
                    ReviewCard(
                        review = review,
                        isOwn = isOwn,
                        onEdit = { onEditReview(review) },
                        onDelete = { onDeleteReview(review.id) }
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun ReviewCard(
    review: Review,
    isOwn: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val displayName = review.user?.displayName ?: "?"
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayName.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = review.user?.username?.let { "@$it" } ?: displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = review.createdAt.take(10),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (isOwn) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            StarRow(rating = review.rating.toFloat(), starSize = 14)
            if (!review.reviewText.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = review.reviewText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MediaCover(detail: MediaDetail) {
    val containerColor = when (detail.mediaType) {
        "book" -> MaterialTheme.colorScheme.primaryContainer
        "movie" -> MovieContainer
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    Box(
        modifier = Modifier
            .size(width = 110.dp, height = 160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        if (detail.coverUrl != null) {
            AsyncImage(
                model = detail.coverUrl,
                contentDescription = detail.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = when (detail.mediaType) {
                    "book" -> "📖"
                    "movie" -> "🎬"
                    else -> "📺"
                },
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun RatingSummary(averageRating: Float, ratingCount: Int) {
    if (ratingCount <= 0) {
        Text(
            text = stringResource(R.string.detail_not_yet_rated),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        StarRow(rating = averageRating)
        Spacer(Modifier.width(6.dp))
        Text(
            text = "%.1f".format(averageRating),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "(${"%,d".format(ratingCount)})",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StarRow(rating: Float, starSize: Int = 16) {
    val rounded = (rating * 2).roundToInt()
    Row {
        for (i in 1..5) {
            val icon = when {
                rounded >= i * 2 -> Icons.Filled.Star
                rounded == i * 2 - 1 -> Icons.Outlined.StarHalf
                else -> Icons.Outlined.StarBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(starSize.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun StatGrid(detail: MediaDetail) {
    val stats = buildList {
        detail.publishedYear?.let { add(stringResource(R.string.detail_stat_year) to it.toString()) }
        when (detail.mediaType) {
            "book" -> detail.pageCount?.let {
                add(stringResource(R.string.detail_stat_pages) to it.toString())
            }
            "movie" -> detail.runtimeMinutes?.let {
                add(stringResource(R.string.detail_stat_runtime) to stringResource(R.string.detail_runtime_minutes, it))
            }
            "show" -> detail.seasonCount?.let {
                add(stringResource(R.string.detail_stat_seasons) to it.toString())
            }
        }
        detail.genres.firstOrNull()?.let {
            add(stringResource(R.string.detail_stat_genre) to it)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stats.forEach { (label, value) ->
            StatBox(label = label, value = value, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SectionCaption(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}