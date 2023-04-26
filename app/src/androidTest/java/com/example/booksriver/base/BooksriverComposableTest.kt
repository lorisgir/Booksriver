package com.example.booksriver.base


import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule


abstract class BooksriverComposableTest {

    @JvmField
    @Rule
    val composeTestRule = createComposeRule()

    fun runTest(body: ComposeContentTestRule.() -> Unit) = composeTestRule.run(body)
}
