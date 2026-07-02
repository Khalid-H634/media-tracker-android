package edu.metrostate.ics342.mediatracker.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.theme.*

@Composable
fun StatusBadge(
    status: LibraryStatus,
    modifier: Modifier = Modifier
) {
    val (containerColor, contentColor) = when (status) {
        LibraryStatus.WANT_TO -> WantToContainer to WantTo
        LibraryStatus.IN_PROGRESS -> InProgressContainer to InProgress
        LibraryStatus.FINISHED -> FinishedContainer to Finished
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = containerColor
    ) {
        Text(
            text = stringResource(status.labelRes),
            color = contentColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}