package com.example.booksriver.manager

import android.content.SharedPreferences
import com.example.booksriver.data.model.User
import com.example.booksriver.session.SessionManager
import com.google.gson.Gson
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence

class SessionManagerTest : BehaviorSpec({
    val preferenceEditor: SharedPreferences.Editor = mockk {
        every { putString(any(), any()) } returns this
        every { clear() } returns this
        every { commit() } returns true
    }

    val preference: SharedPreferences = mockk(relaxUnitFun = true) {
        every { edit() } returns preferenceEditor
    }

    val manager = SessionManager(preference)

    Given("A authentication token") {
        val user = User(1, "username", "email", "Bearer ABCD")
        val expectedToken = "Bearer ABCD"
        val expectedUserJson = Gson().toJson(user)

        every { preference.getString("auth_token", any()) } returns expectedToken
        every { preference.getString("auth_user", any()) } returns expectedUserJson

        When("The token is saved in the preference storage") {
            manager.saveUser(user)

            Then("Token should get saved in the preference storage") {
                verifySequence {
                    preference.edit()
                    preferenceEditor.putString("auth_user", expectedUserJson)
                    preferenceEditor.commit()
                    preference.edit()
                    preferenceEditor.putString("auth_token", expectedToken)
                    preferenceEditor.commit()
                }
            }
        }

        When("The token is retrieved from storage") {
            val actualToken = manager.getToken()

            Then("Manager should request token from preference storage") {
                verify(exactly = 1) { preference.getString("auth_token", null) }
            }

            Then("Retrieved token should be valid") {
                actualToken shouldBe expectedToken
            }
        }

        When("The user is retrieved from storage") {
            val actualUser = manager.getUser()

            Then("Manager should request user from preference storage") {
                verify(exactly = 1) { preference.getString("auth_user", null) }
            }

            Then("Retrieved user should be valid") {
                actualUser shouldBe user
            }
        }
    }
})
