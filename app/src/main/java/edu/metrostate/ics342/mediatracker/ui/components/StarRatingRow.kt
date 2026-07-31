package edu.metrostate.ics342.mediatracker.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingRow(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    starSize: Int = 32
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            IconButton(
                onClick = { onRatingChange(i) }
            ) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Star $i",
                    tint = if (i <= rating) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    },
                    modifier = Modifier
                )
            }
        }
    }
}