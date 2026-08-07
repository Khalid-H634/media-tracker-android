package edu.metrostate.ics342.mediatracker.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class StarRatingRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun starRatingRow_tappingThirdStar_callsOnRatingChangeWith3() {
        var rating = 0
        composeTestRule.setContent {
            StarRatingRow(
                rating = rating,
                onRatingChange = { newRating -> rating = newRating }
            )
        }

        composeTestRule.onNodeWithContentDescription("Star 3").performClick()
        assertEquals(3, rating)
    }

    @Test
    fun starRatingRow_tappingFirstStarAfterFifth_lowersRating() {
        var rating = 5
        composeTestRule.setContent {
            StarRatingRow(
                rating = rating,
                onRatingChange = { newRating -> rating = newRating }
            )
        }

        composeTestRule.onNodeWithContentDescription("Star 1").performClick()
        assertEquals(1, rating)
    }
}