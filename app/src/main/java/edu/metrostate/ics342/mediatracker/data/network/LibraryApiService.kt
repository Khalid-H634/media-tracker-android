package edu.metrostate.ics342.mediatracker.data.network

import retrofit2.Response
import retrofit2.http.*

interface LibraryApiService {
    @GET("library")
    suspend fun getLibrary(): Response<List<LibraryItemResponse>>

    @GET("library/{mediaId}")
    suspend fun getLibraryItem(
        @Path("mediaId") mediaId: Int
    ): Response<LibraryItemResponse>

    @POST("library")
    suspend fun addToLibrary(
        @Body request: AddToLibraryRequest
    ): Response<Unit>

    @DELETE("library/{mediaId}")
    suspend fun removeFromLibrary(
        @Path("mediaId") mediaId: Int
    ): Response<Unit>
}

data class AddToLibraryRequest(
    val mediaId: Int,
    val status: String
)