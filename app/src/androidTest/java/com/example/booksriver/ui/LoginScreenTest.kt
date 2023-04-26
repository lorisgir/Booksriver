package com.example.booksriver.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.booksriver.base.BooksriverScreenTest
import com.example.booksriver.ui.login.LoginScreen
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalComposeUiApi::class)
@HiltAndroidTest
class LoginScreenTest : BooksriverScreenTest() {

    @Test
    fun navigateToSignup_onClickingSignupText() = runTest {
        var navigatingToSignup = false
        setBooksriverContent {
            TestLoginScreen(onNavigateToSignup = { navigatingToSignup = true })
        }

        onNodeWithText("Don't have an account? Sign Up!").performClick()
        waitForIdle()

        assertTrue(navigatingToSignup)
    }

    @Test
    fun showDoNothing_whenEnteredInvalidCredentials() = runTest {
        var navigatedToMain = false
        val closeKeyboard = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
        setBooksriverContent {
            TestLoginScreen(
                closeKeyboard = closeKeyboard,
                onNavigateToMain = { navigatedToMain = true }
            )
        }

        onNodeWithTag("Username").performTextInput("")
        waitForIdle()
        closeKeyboard.tryEmit(Unit)
        waitForIdle()

        onNodeWithTag("Password").performTextInput("")
        closeKeyboard.tryEmit(Unit)
        waitForIdle()

        onNodeWithTag("LoginBtn").performClick()
        waitForIdle()

        assertFalse(navigatedToMain)
    }

    @Test
    fun showDoNotExistError_whenEnteredWrongCredentials() = runTest {
        var navigatedToMain = false
        val closeKeyboard = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
        setBooksriverContent {
            TestLoginScreen(
                closeKeyboard = closeKeyboard,
                onNavigateToMain = { navigatedToMain = true }
            )
        }

        onNodeWithTag("Username").performTextInput("asdfgh")
        waitForIdle()
        closeKeyboard.tryEmit(Unit)
        waitForIdle()

        onNodeWithTag("Password").performTextInput("123456")
        closeKeyboard.tryEmit(Unit)
        waitForIdle()

        onNodeWithTag("LoginBtn").performClick()

        Thread.sleep(1000)

        assertFalse(navigatedToMain)
        onNodeWithText("Bad credentials").assertExists()
    }

    @Test
    fun navigateToMain_onSuccessfulLogin() = runTest {
        var navigatedToMain = false
        val closeKeyboard = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
        setBooksriverContent {
            TestLoginScreen(
                closeKeyboard = closeKeyboard,
                onNavigateToMain = {
                    navigatedToMain = true
                }
            )
        }

        onNodeWithTag("Username").performTextInput("123456")
        waitForIdle()
        closeKeyboard.tryEmit(Unit)
        waitForIdle()


        onNodeWithTag("Password").performTextInput("123456")
        closeKeyboard.tryEmit(Unit)
        waitForIdle()


        onNodeWithTag("LoginBtn").performClick()

        Thread.sleep(1000)

        onNodeWithText("Bad credentials").assertDoesNotExist()
    }

    @Composable
    private fun TestLoginScreen(
        closeKeyboard: Flow<Unit> = emptyFlow(),
        onNavigateToSignup: () -> Unit = {},
        onNavigateToMain: () -> Unit = {}
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        LoginScreen(
            viewModel = viewModel(),
            onNavigateToSignUp = onNavigateToSignup,
            onNavigateToMain = onNavigateToMain
        )

        LaunchedEffect(Unit) {
            closeKeyboard.collect { keyboardController?.hide() }
        }
    }
}
