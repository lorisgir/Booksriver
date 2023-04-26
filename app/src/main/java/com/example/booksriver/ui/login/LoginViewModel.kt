package com.example.booksriver.ui.login

import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.model.User
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.repository.AuthRepository
import com.example.booksriver.repository.Either
import com.example.booksriver.session.SessionManager
import com.example.booksriver.util.BooksriverValidators
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : BaseViewModel<LoginState>(initialState = LoginState()) {

    fun setUsername(username: String) {
        setState { state -> state.copy(username = username) }
    }

    fun setPassword(password: String) {
        setState { state -> state.copy(password = password) }
    }

    fun dismissFailureDialog() {
        setState { state -> state.copy(showFailureDialog = false) }
    }


    fun googleLogin(task: Task<GoogleSignInAccount>?) {
        viewModelScope.launch {
            val account = task?.result
            if (account == null) {
                setState { state ->
                    state.copy(
                        error = "Google sign in failed",
                        showFailureDialog = true
                    )
                }
            } else {
                val response = authRepository.socialLogin(account.email!!, account.id!!)
                onAfterResponse(response)
            }
        }
    }

    fun login() {
        if (!validateCredentials()) return

        viewModelScope.launch {
            val username = currentState.username
            val password = currentState.password

            setState { state -> state.copy(isLoading = true) }

            val response = authRepository.login(username, password)
            onAfterResponse(response)
        }
    }

    private fun onAfterResponse(response: Either<out User, out ErrorResponse>) {
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

    private fun validateCredentials(): Boolean {
        val username = currentState.username
        val password = currentState.password

        val isValidUsername = BooksriverValidators.isNotNull(username)
        val isValidPassword = BooksriverValidators.isNotNull(password)


        setState { state ->
            state.copy(
                isValidUsername = isValidUsername,
                isValidPassword = isValidPassword,
            )
        }

        return isValidUsername && isValidPassword
    }
}
