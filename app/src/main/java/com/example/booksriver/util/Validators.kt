package com.example.booksriver.util

import android.text.TextUtils

object BooksriverValidators {
    fun isNotNull(value: String): Boolean = value.isNotEmpty()
    fun isNumber(value: String): Boolean = value.toIntOrNull() != null
    fun isValidUsername(username: String): Boolean = username.trim().length in (4..30)
    fun isValidPassword(password: String): Boolean = password.trim().length in (6..50)
    fun isValidEmail(email: String): Boolean =
        !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidReview(score: Double, text: String) = isValidScore(score) && text.isNotEmpty()

    fun isValidScore(score: Double) = score in 0.5..5.0

    fun isPasswordAndConfirmPasswordSame(
        password: String,
        confirmedPassword: String
    ): Boolean = password.trim() == confirmedPassword.trim()
}
