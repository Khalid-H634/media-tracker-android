package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.model.creatorCredit
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    onWriteReview: (Int) -> Unit,
    viewModel: MediaDetailViewModel = viewModel()
) {
    LaunchedEffect(mediaId) { viewModel.setMediaId(mediaId) }
    val media by viewModel.media.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.action_back))
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.MoreVert, stringResource(R.string.action_more_options))
                    }
                }
            )
        }
    ) { padding ->
        if (media == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val reviews = getHardcodedReviews(mediaId)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (media!!.mediaType) { "book" -> "📖" "movie" -> "🎬" "show" -> "📺" else -> "?" },
                        fontSize = 64.sp
                    )
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(media.mediaType.replaceFirstChar { it.uppercase() }, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            item {
                Column {
                    Text(media!!.title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(media.creatorCredit(LocalContext.current), fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            when {
                                index < media!!.averageRating.toInt() -> Icons.Filled.Star
                                index < media.averageRating -> Icons.Filled.StarHalf
                                else -> Icons.Outlined.Star
                            },
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(" ${String.format("%.1f", media.averageRating)}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(" (${media.ratingCount})", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button({}, Modifier.weight(1f), RoundedCornerShape(8.dp)) { Text("+ Want To") }
                    OutlinedButton({}, Modifier.weight(1f), RoundedCornerShape(8.dp)) {
                        Icon(Icons.Filled.FavoriteBorder, null, Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Save")
                    }
                }
            }

            item {
                Column {
                    Text("About", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(media!!.description ?: "No description.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBox("Year", media!!.publishedYear?.toString() ?: "N/A", Modifier.weight(1f))
                    StatBox(getStatLabel(media), getStatValue(media), Modifier.weight(1f))
                    StatBox("Genre", media.genres.firstOrNull() ?: "N/A", Modifier.weight(1f))
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Reviews (${reviews.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    TextButton({ onWriteReview(mediaId) }) { Text("+ Write Review") }
                }
            }

            items(reviews) { review ->
                ReviewCard(review)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(review.user?.displayName?.firstOrNull()?.toString()?.uppercase() ?: "?", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(review.user?.displayName ?: "Unknown", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(formatTimestamp(review.createdAt), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(5) { index ->
                        Icon(
                            if (index < review.rating) Icons.Filled.Star else Icons.Outlined.Star,
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            if (!review.reviewText.isNullOrBlank()) {
                Text(review.reviewText, fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
    }
}

private fun getStatLabel(media: Media) = when (media.mediaType) {
    "book" -> "Pages"
    "movie" -> "Runtime"
    "show" -> "Episodes"
    else -> "Detail"
}

private fun getStatValue(media: Media) = "N/A"

private fun formatTimestamp(timestamp: String): String {
    return try {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(timestamp)
        date?.let { sdf.format(it) } ?: timestamp
    } catch (e: Exception) { timestamp }
}

private fun getHardcodedReviews(mediaId: Int) = listOf(
    Review("user-001", mediaId, 5, "Absolutely loved this! Highly recommend!", "2024-01-15T10:30:00Z", FakeMediaRepository.currentUser),
    Review("user-002", mediaId, 4, "Really enjoyed it, though pacing was a bit slow.", "2024-01-18T14:20:00Z", FakeMediaRepository.followers.firstOrNull()),
    Review("user-003", mediaId, 3, "Good but not great. Fell short in some areas.", "2024-01-20T09:15:00Z", FakeMediaRepository.following.firstOrNull())
)