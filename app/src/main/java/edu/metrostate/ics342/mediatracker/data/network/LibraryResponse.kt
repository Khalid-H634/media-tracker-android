package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import kotlinx.serialization.Serializable

@Serializable
data class LibraryItemResponse(
    val userId: String,
    val mediaId: Int,
    val status: String,
    val addedAt: String,
    val updatedAt: String,
    val media: Media? = null
)

fun LibraryItemResponse.toLibraryItem(): LibraryItem {
    return LibraryItem(
        userId = userId,
        mediaId = mediaId,
        status = LibraryStatus.fromString(status),
        addedAt = addedAt,
        updatedAt = updatedAt,
        media = media ?: Media(
            id = mediaId,
            mediaType = "unknown",
            title = "Unknown Title"
        )
    )
}