package com.example.booksriver.validator

import com.example.booksriver.util.BooksriverValidators
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

class AuthValidatorTest : BehaviorSpec({
    Given("Usernames") {
        And("Usernames are valid") {
            val usernames = listOf("loros", "ghibo", "pippo")

            When("Usernames are validated") {
                val areUsernamesValid = usernames.map { BooksriverValidators.isValidUsername(it) }

                Then("Usernames should be valid") {
                    areUsernamesValid shouldContain true
                    areUsernamesValid shouldNotContain false
                }
            }
        }

        And("Usernames are invalid") {
            val usernames = listOf("123", "ape", "    hey    ")

            When("Usernames are validated") {
                val areUsernamesValid = usernames.map { BooksriverValidators.isValidUsername(it) }

                Then("Usernames should be invalid") {
                    areUsernamesValid shouldContain false
                    areUsernamesValid shouldNotContain true
                }
            }
        }
    }

    Given("Passwords") {
        And("Passwords are valid") {
            val passwords = listOf("123456", "asdfghdfgh", "sda78asd~!*%%!%^&")

            When("Passwords are validated") {
                val arePasswordsValid = passwords.map { BooksriverValidators.isValidPassword(it) }

                Then("Passwords should be valid") {
                    arePasswordsValid shouldContain true
                    arePasswordsValid shouldNotContain false
                }
            }
        }

        And("Passwords are invalid") {
            val passwords = listOf(
                "12345",
                "   hey       ",
                "123456789012345678901234567890123456789012345678901234567890" // More than 50 chars
            )

            When("Passwords are validated") {
                val arePasswordsValid = passwords.map { BooksriverValidators.isValidPassword(it) }

                Then("Passwords should be invalid") {
                    arePasswordsValid shouldContain false
                    arePasswordsValid shouldNotContain true
                }
            }
        }
    }

    Given("Password and confirm password") {
        And("Both are same") {
            val password = "password1234"
            val confirmPassword = "password1234"

            When("They are checked whether they are same") {
                val areSame = BooksriverValidators.isPasswordAndConfirmPasswordSame(
                    password = password,
                    confirmedPassword = confirmPassword
                )

                Then("They should be the same") {
                    areSame shouldBe true
                }
            }
        }

        And("Both are NOT same") {
            val password = "password"
            val confirmPassword = "wrongPassword"

            When("They are checked whether they are same") {
                val areSame = BooksriverValidators.isPasswordAndConfirmPasswordSame(
                    password = password,
                    confirmedPassword = confirmPassword
                )

                Then("They should NOT be the same") {
                    areSame shouldBe false
                }
            }
        }
    }
})
