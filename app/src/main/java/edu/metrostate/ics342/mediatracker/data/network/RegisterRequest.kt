package edu.metrostate.ics342.mediatracker.data.network

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val displayName: String,
    val clientId: String = ApiConstants.CLIENT_ID,
    val clientSecret: String = ApiConstants.CLIENT_SECRET
)