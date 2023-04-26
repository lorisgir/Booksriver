package com.example.booksriver.ui.login

import com.example.booksriver.view.state.IState

data class LoginState(
    val isLoading: Boolean = false,
    val showFailureDialog: Boolean = false,
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val password: String = "",
    val isValidUsername: Boolean? = null,
    val isValidPassword: Boolean? = null,
    val error: String? = null
) : IState
