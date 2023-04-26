package com.example.booksriver.base

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.booksriver.MainActivity
import com.example.booksriver.TestActivity
import com.example.booksriver.theme.BooksriverTheme
import com.example.booksriver.view.BaseViewModel
import com.example.booksriver.view.state.IState
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Rule

/**
 * Base spec for testing Jetpack Compose screens
 *
 * This takes care of instantiating Hilt, WorkManager.
 */
@Suppress("LeakingThis")
abstract class BooksriverScreenTest {
    @JvmField
    @Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @JvmField
    @Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    fun inject() = hiltRule.inject()

    inline fun <reified T : BaseViewModel<out IState>> viewModel() =
        composeTestRule.activity.viewModels<T>().value

    fun runTest(
        body: AndroidComposeTestRule<ActivityScenarioRule<TestActivity>, TestActivity>.() -> Unit
    ) = composeTestRule.run(body)

    fun setBooksriverContent(content: @Composable () -> Unit) = composeTestRule.setContent {
        BooksriverTheme() {
            content()
        }
    }
}
