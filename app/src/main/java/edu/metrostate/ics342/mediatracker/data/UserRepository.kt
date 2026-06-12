package edu.metrostate.ics342.mediatracker.data

import edu.metrostate.ics342.mediatracker.data.model.CreateUserRequest
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val baseURL = "https://wjtzkgpxmxtzcczzbvrz.supabase.co/functions/v1/"

class UserRepository {
    private val api: ApiService = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    suspend fun createAccount(
        displayName: String,
        username: String,
        email: String,
        password: String
    ): Boolean {
        return try {
            val createUserRequest = CreateUserRequest(
                email = email,
                password = password,
                username = username,
                displayName = displayName,
                clientId = "",
                clientSecret = ""
            )
            val response = api.createUser(createUserRequest)
            response.id.isNotEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}