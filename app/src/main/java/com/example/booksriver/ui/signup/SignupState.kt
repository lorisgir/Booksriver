package com.example.booksriver.ui.signup

import com.example.booksriver.view.state.IState

data class SignupState(
    val isLoading: Boolean = false,
    val showFailureDialog: Boolean = false,
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isValidUsername: Boolean? = null,
    val isValidEmail: Boolean? = null,
    val isValidPassword: Boolean? = null,
    val isValidConfirmPassword: Boolean? = null,
    val error: String? = null
) : IState
