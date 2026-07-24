package edu.metrostate.ics342.mediatracker.data.network

import retrofit2.Response
import retrofit2.http.*

interface FavoritesApiService {
    @GET("favorites/{mediaId}")
    suspend fun getFavorite(
        @Path("mediaId") mediaId: Int
    ): Response<FavoriteResponse>

    @POST("favorites")
    suspend fun addFavorite(
        @Body request: AddFavoriteRequest
    ): Response<Unit>

    @DELETE("favorites/{mediaId}")
    suspend fun removeFavorite(
        @Path("mediaId") mediaId: Int
    ): Response<Unit>
}

data class AddFavoriteRequest(
    val mediaId: Int
)