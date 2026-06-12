package edu.metrostate.ics342.mediatracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserResponse(
    val id: String,
    val email: String,
    val username: String,
    val displayName: String,
    val message: String? = null
)