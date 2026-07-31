package edu.metrostate.ics342.mediatracker.data.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository

object RetrofitInstance {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private lateinit var authClient: OkHttpClient

    fun initialize(context: Context) {
        val sessionRepository = DefaultSessionRepository(context)

        authClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionRepository))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(authClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val userApiService: UserApiService by lazy { retrofit.create(UserApiService::class.java) }
    val mediaApiService: MediaApiService by lazy { retrofit.create(MediaApiService::class.java) }
    val libraryApiService: LibraryApiService by lazy { retrofit.create(LibraryApiService::class.java) }
    val favoritesApiService: FavoritesApiService by lazy { retrofit.create(FavoritesApiService::class.java) }
    val reviewApiService: ReviewApiService by lazy { retrofit.create(ReviewApiService::class.java)
    }
}