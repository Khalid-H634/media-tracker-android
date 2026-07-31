package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.model.Review
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.*

@Serializable
data class CreateReviewRequest(
    val mediaId: Int,
    val rating: Int,
    val reviewText: String? = null,
    val shareToFeed: Boolean? = null
)

@Serializable
data class UpdateReviewRequest(
    val rating: Int,
    val reviewText: String? = null,
    val shareToFeed: Boolean? = null
)

interface ReviewApiService {
    @GET("reviews")
    suspend fun getReviews(
        @Query("mediaId") mediaId: Int
    ): Response<List<Review>>

    @POST("reviews")
    suspend fun createReview(
        @Body request: CreateReviewRequest
    ): Response<Review>

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") reviewId: Int,
        @Body request: UpdateReviewRequest
    ): Response<Review>

    @DELETE("reviews/{id}")
    suspend fun deleteReview(
        @Path("id") reviewId: Int
    ): Response<Unit>
}