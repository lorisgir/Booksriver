package com.example.booksriver.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import com.example.booksriver.base.BooksriverComposableTest
import com.example.booksriver.component.ConnectivityStatus
import org.junit.Test

class ConnectivityStatusTest : BooksriverComposableTest() {


    @Test
    fun testNoConnectivity() = runTest {
        setContent {
            ConnectivityStatus(isConnected = false)
        }

        onNodeWithText("No Internet Connection!").assertIsDisplayed()
    }

    @Test
    fun testConnectivityBackAfterNoConnectivity() = runTest {
        var isConnected by mutableStateOf(false)
        setContent {
            ConnectivityStatus(isConnected = isConnected)
        }

        // First, no connectivity should be displayed
        onNodeWithText("No Internet Connection!").assertIsDisplayed()

        // Bring connectivity back
        isConnected = true
        onNodeWithText("No Internet Connection!").assertDoesNotExist()
        onNodeWithText("Back Online!").assertIsDisplayed()

        // Wait for 2 seconds
        mainClock.advanceTimeBy(2000)

        // Status should be vanished
        onNodeWithText("Back Online!").assertDoesNotExist()
    }
}
