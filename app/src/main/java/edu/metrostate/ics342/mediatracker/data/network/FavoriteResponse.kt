package edu.metrostate.ics342.mediatracker.data.network

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponse(
    val userId: String,
    val mediaId: Int,
    val createdAt: String
)