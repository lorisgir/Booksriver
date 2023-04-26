package com.example.booksriver.repository


import com.example.booksriver.data.model.User
import com.example.booksriver.data.request.LoginRequest
import com.example.booksriver.data.request.LoginSocialRequest
import com.example.booksriver.data.request.RegisterRequest
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.AuthService
import com.google.gson.Gson
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.spyk
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class UserRepositoryTest : BehaviorSpec({

    val authService = spyk(FakeAuthService())
    val repository = AuthRepository(authService)

    Given("A user") {
        When("New user is added") {
            And("Credentials are valid") {
                val response = repository.addUser(
                    "admin", "admin@gmail.com", "admin", "admin"
                )

                Then("User should be get added") {
                    coVerify {
                        authService.register(
                            RegisterRequest(
                                "admin", "admin@gmail.com", "admin", "admin"
                            )
                        )
                    }
                }

                Then("Valid response with token should be returned") {
                    val credentials = (response as Either.Success).data
                    credentials.token shouldBe "Bearer ABCD"
                }
            }

            And("Credentials are invalid") {
                val response = repository.addUser(
                    "ghibo", "ghibo@gmail.com", "123", "123"
                )

                Then("User should be get added") {
                    coVerify {
                        authService.register(
                            RegisterRequest(
                                "ghibo", "ghibo@gmail.com", "123", "123"
                            )
                        )
                    }
                }

                Then("Valid response with error message should be returned") {
                    val message = (response as Either.Error).error.message
                    message shouldBe "Bad Request Error"
                }
            }
        }

        When("A user is retrieved by credentials") {
            And("Credentials are valid") {
                val response = repository.login("admin", "admin")

                Then("User login should be get requested") {
                    coVerify { authService.login(LoginRequest("admin", "admin")) }
                }

                Then("Valid response with token should be returned") {
                    val credentials = (response as Either.Success).data
                    credentials.token shouldBe "Bearer ABCD"
                }
            }

            And("Credentials are invalid") {
                val response = repository.login("ghibo", "123")

                Then("User login should be get requested") {
                    coVerify { authService.login(LoginRequest("ghibo", "123")) }
                }

                Then("Valid response with error message should be returned") {
                    val message = (response as Either.Error).error.message
                    message shouldBe "Bad Request Error"
                }
            }
        }
    }
})


class FakeAuthService : AuthService {
    override suspend fun register(registerRequest: RegisterRequest): Response<User> {
        return fakeAuthResponse(registerRequest.username, registerRequest.password)
    }

    override suspend fun login(loginRequest: LoginRequest): Response<User> {
        return fakeAuthResponse(loginRequest.username, loginRequest.password)
    }

    override suspend fun loginSocial(loginSocialRequest: LoginSocialRequest): Response<User> {
        TODO("Not yet implemented")
    }

    private fun fakeAuthResponse(username: String, password: String): Response<User> {
        return if (username == "admin" && password == "admin") {
            Response.success(
                User(1, "username", "email"),
                okhttp3.Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_2)
                    .request(Request.Builder().url("http://localhost/").build())
                    .addHeader("authorization", "Bearer ABCD")
                    .build()
            )
        } else {
            val response = ErrorResponse("Bad Request Error", emptyList())
            val body =
                Gson().toJson(response).toResponseBody("application/json".toMediaTypeOrNull())
            Response.error(401, body)
        }
    }
}

