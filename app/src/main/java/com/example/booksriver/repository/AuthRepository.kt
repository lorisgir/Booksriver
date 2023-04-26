@file:Suppress("UNCHECKED_CAST")

package com.example.booksriver.repository


import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.Profile
import com.example.booksriver.data.model.User
import com.example.booksriver.data.request.ChangeUsernameRequest
import com.example.booksriver.data.request.LoginRequest
import com.example.booksriver.data.request.LoginSocialRequest
import com.example.booksriver.data.request.RegisterRequest
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.AuthService
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.util.getDefault
import com.example.booksriver.util.getDefaultFromDto
import com.example.booksriver.util.getDefaultListFromDto
import com.example.booksriver.util.getErrorResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject internal constructor(
    private val authService: AuthService
) {

    private fun getAuthResponse(authResponse: Response<User>): Either<out User, out ErrorResponse> {
        return if (authResponse.isSuccessful) {
            Either.success(
                authResponse.body()!!
                    .setTokenAndGet(authResponse.headers()["authorization"]!!)
            )
        } else {
            Either.error(authResponse.getErrorResponse())
        }
    }

    suspend fun addUser(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Either<out User, out ErrorResponse> {
        return runCatching {
            val authResponse = authService.register(
                RegisterRequest(
                    username,
                    email,
                    password,
                    confirmPassword
                )
            )
            getAuthResponse(authResponse)
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun socialLogin(email: String, id: String): Either<out User, out ErrorResponse> {
        return runCatching {
            val authResponse =
                authService.loginSocial(LoginSocialRequest(email, id))
            getAuthResponse(authResponse)
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun login(username: String, password: String): Either<out User, out ErrorResponse> {
        return runCatching {
            val authResponse =
                authService.login(LoginRequest(username, password))
            getAuthResponse(authResponse)
        }.getOrDefault(Either.error(ErrorResponse()))
    }
}
