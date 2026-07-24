package edu.metrostate.ics342.mediatracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import edu.metrostate.ics342.mediatracker.navigation.MediaTrackerNavGraph
import edu.metrostate.ics342.mediatracker.data.network.RetrofitInstance
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import edu.metrostate.ics342.mediatracker.data.model.UserProfile
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitInstance.initialize(applicationContext)

        runBlocking {
            val sessionRepo = DefaultSessionRepository(applicationContext)
            val existingToken = sessionRepo.getAccessToken()

            if (existingToken == null) {
                // PASTE YOUR REAL TOKEN HERE
                val realToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." // ← Replace this
                val testUser = UserProfile(
                    id = "test-user-1",
                    email = "test@example.com",
                    username = "testuser",
                    displayName = "Test User"
                )
                sessionRepo.saveSession(
                    accessToken = realToken,
                    refreshToken = realToken,
                    user = testUser
                )
                android.util.Log.d("MainActivity", "Real token saved: ${realToken.take(20)}...")
            }
        }

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    MediaTrackerNavGraph(navController = navController)
}
/* package edu.metrostate.ics342.mediatracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import edu.metrostate.ics342.mediatracker.navigation.MediaTrackerNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    MediaTrackerNavGraph(navController = navController)
}



 */