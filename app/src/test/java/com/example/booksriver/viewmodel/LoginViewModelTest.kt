package com.example.booksriver.viewmodel


import com.example.booksriver.data.model.User
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.repository.AuthRepository
import com.example.booksriver.repository.Either
import com.example.booksriver.session.SessionManager
import com.example.booksriver.ui.login.LoginState
import com.example.booksriver.ui.login.LoginViewModel
import com.example.booksriver.viewmodel.base.ViewModelBehaviorSpec
import com.example.booksriver.viewmodel.utils.currentStateShouldBe
import com.example.booksriver.viewmodel.utils.withState
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify

class LoginViewModelTest : ViewModelBehaviorSpec({

    val repository: AuthRepository = mockk()
    val sessionManager: SessionManager = mockk(relaxUnitFun = true)

    val viewModel = LoginViewModel(repository, sessionManager)

    Given("The ViewModel") {
        When("Initialized") {
            val expectedState = LoginState(
                isLoading = false,
                isLoggedIn = false,
                showFailureDialog = false,
                error = null,
                username = "",
                password = "",
                isValidUsername = null,
                isValidPassword = null
            )
            Then("Initial state should be valid") {
                viewModel currentStateShouldBe expectedState
            }
        }
    }

    Given("A username and password") {
        val username = "ghibo"
        val password = "123456"

        When("Username is set") {
            viewModel.setUsername(username)

            Then("Username should be updated in the current state") {
                viewModel.withState { this.username shouldBe username }
            }
        }

        When("Password is set") {
            viewModel.setPassword(password)

            Then("Password should be updated in the current state") {
                viewModel.withState { this.password shouldBe password }
            }
        }
    }

    Given("A user credentials") {
        And("The user provides incomplete credentials") {
            val username = ""
            val password = ""

            viewModel.setUsername(username)
            viewModel.setPassword(password)

            When("User logs in") {
                viewModel.login()

                Then("Credentials should be validated and state should be updated") {
                    viewModel.withState {
                        isValidUsername shouldBe false
                        isValidPassword shouldBe false
                    }
                }

                Then("User should NOT be get retrieved") {
                    coVerify(exactly = 0) {
                        repository.login(username, password)
                    }
                }
            }
        }

        And("User uses valid credentials") {
            val username = "ghibo"
            val password = "123456"

            viewModel.setUsername(username)
            viewModel.setPassword(password)

            val token = "Bearer ABCD"
            val user = User(1, username, username, token)

            coEvery { repository.login(username, password) }.returns(Either.success(user))

            When("User logs in") {
                viewModel.login()

                Then("Credentials should be validated") {
                    viewModel.withState {
                        isValidUsername shouldBe true
                        isValidPassword shouldBe true
                    }
                }

                Then("User should be get retrieved") {
                    coVerify { repository.login(username, password) }
                }

                Then("Authentication token should be get saved") {
                    verify { sessionManager.saveUser(eq(user)) }
                }

                Then("Valid UI states should be updated") {
                    viewModel.withState {
                        isLoading shouldBe false
                        isLoggedIn shouldBe true
                        error shouldBe null
                    }
                }
            }
        }

        And("Repository fails to fulfil the request") {
            val username = "ghibo"
            val password = "123456"

            viewModel.setUsername(username)
            viewModel.setPassword(password)

            coEvery { repository.login(username, password) }
                .returns(Either.error(ErrorResponse("User not exist")))

            When("User logs in") {
                viewModel.login()

                Then("User should be get retrieved") {
                    coVerify { repository.login(username, password) }
                }

                Then("State should contain error") {
                    viewModel.withState {
                        isLoggedIn shouldBe false
                        showFailureDialog shouldBe true
                        error shouldBe "User not exist"
                    }
                }
            }
        }
    }
})
