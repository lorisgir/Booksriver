package com.example.booksriver.ui.signup

import androidx.lifecycle.viewModelScope
import com.example.booksriver.repository.AuthRepository
import com.example.booksriver.session.SessionManager
import com.example.booksriver.util.BooksriverValidators
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : BaseViewModel<SignupState>(initialState = SignupState()) {

    fun setUsername(username: String) {
        setState { state -> state.copy(username = username) }
    }

    fun setEmail(email: String) {
        setState { state -> state.copy(email = email) }
    }

    fun setPassword(password: String) {
        setState { state -> state.copy(password = password) }
    }

    fun setConfirmPassword(password: String) {
        setState { state -> state.copy(confirmPassword = password) }
    }

    fun dismissFailureDialog() {
        setState { state -> state.copy(showFailureDialog = false) }
    }


    fun register() {
        if (!validateCredentials()) return

        viewModelScope.launch {
            val username = currentState.username
            val password = currentState.password
            val confirmPassword = currentState.confirmPassword
            val email = currentState.email

            setState { state -> state.copy(isLoading = true) }

            val response = authRepository.addUser(username, email, password, confirmPassword)

            response.onSuccess { user ->
                sessionManager.saveUser(user)
                setState { state -> state.copy(isLoading = false, isLoggedIn = true, error = null) }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        isLoading = false,
                        error = getDetailsOrMessage(error),
                        showFailureDialog = true,
                        isLoggedIn = false
                    )
                }
            }
        }
    }

    private fun validateCredentials(): Boolean {
        val username = currentState.username
        val email = currentState.email
        val password = currentState.password
        val confirmPassword = currentState.confirmPassword

        val isValidUsername = BooksriverValidators.isValidUsername(username)
        val isValidEmail = BooksriverValidators.isValidEmail(email)
        val isValidPassword = BooksriverValidators.isValidPassword(password)
        val arePasswordAndConfirmPasswordSame =
            isValidPassword && BooksriverValidators.isPasswordAndConfirmPasswordSame(
                password,
                confirmPassword
            )

        setState { state ->
            state.copy(
                isValidUsername = isValidUsername,
                isValidEmail = isValidEmail,
                isValidPassword = isValidPassword,
                isValidConfirmPassword = arePasswordAndConfirmPasswordSame
            )
        }

        return isValidUsername && isValidEmail && isValidPassword && arePasswordAndConfirmPasswordSame
    }
}
