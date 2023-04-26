package com.example.booksriver.network.api

import com.example.booksriver.data.model.User
import com.example.booksriver.data.request.LoginRequest
import com.example.booksriver.data.request.LoginSocialRequest
import com.example.booksriver.data.request.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/user-server/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<User>

    @POST("/user-server/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<User>

    @POST("/user-server/auth/login-social")
    suspend fun loginSocial(@Body loginSocialRequest: LoginSocialRequest): Response<User>
}