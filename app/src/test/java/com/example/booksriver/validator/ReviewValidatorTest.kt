package com.example.booksriver.validator

import com.example.booksriver.util.BooksriverValidators
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain

class ReviewValidatorTest : BehaviorSpec({
    Given("Review score and review text") {
        And("They are valid") {
            val scoreAndText = listOf(
                0.5 to "review 2",
                1.5 to "review 3",
                5 to "review 4",
            )

            When("Score and text are validated") {
                val areValid = scoreAndText.map { (score, text) ->
                    BooksriverValidators.isValidReview(score.toDouble(), text)
                }

                Then("Review should be valid") {
                    areValid shouldContain true
                    areValid shouldNotContain false
                }
            }
        }

        And("They are invalid") {
            val scoreAndText = listOf(
                0 to "review 1",
                6 to "review 5",
                3 to "",
            )

            When("Score and text are validated") {
                val areInvalid = scoreAndText.map { (score, text) ->
                    BooksriverValidators.isValidReview(score.toDouble(), text)
                }

                Then("Review should be invalid") {
                    areInvalid shouldContain false
                    areInvalid shouldNotContain true
                }
            }
        }
    }
})
