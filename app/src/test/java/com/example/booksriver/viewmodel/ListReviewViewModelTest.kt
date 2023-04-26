package com.example.booksriver.viewmodel


import androidx.lifecycle.SavedStateHandle
import com.example.booksriver.data.model.Review
import com.example.booksriver.data.model.User
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.repository.Either
import com.example.booksriver.repository.ReviewRepository
import com.example.booksriver.ui.review.ReviewState
import com.example.booksriver.ui.review.ReviewViewModel
import com.example.booksriver.viewmodel.base.ViewModelBehaviorSpec
import com.example.booksriver.viewmodel.utils.currentStateShouldBe
import com.example.booksriver.viewmodel.utils.withState
import io.kotest.common.ExperimentalKotest
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalKotest::class)
class ListReviewViewModelTest : ViewModelBehaviorSpec({

    val bookId = 245

    val repository: ReviewRepository = mockk()
    val savedStateHandle = SavedStateHandle().apply {
        set("bookId", bookId.toString())
    }
    val viewModel = ReviewViewModel(repository, savedStateHandle)

    val user = User(1, "a", "b", "c")
    val reviews = listOf(
        Review(1, "asd", 1, 1.2, user),
        Review(2, "123", 2, 2.2, user),
        Review(3, "654", 3, 3.3, user)
    )
    val error = ErrorResponse("Bad request", emptyList())

    Given("The ViewModel") {
        val expectedState = ReviewState(
            isLoading = true,
            error = null,
            reviews = emptyList(),
            hasPersonalReview = null,
            toastMessage = null,
            isRefreshing = false
        )

        When("Initialized") {
            Then("Initial state should be valid") {
                viewModel currentStateShouldBe expectedState
            }
        }
    }

    Given("The list of reviews") {

        And("is successful") {
            coEvery { repository.getReviewsByBookId(any()) }.returns(Either.success(reviews))

            When("The reviews are retrieved successfully") {
                viewModel.getReviews(bookId)

                Then("Review should be retrieved") {
                    coVerify { repository.getReviewsByBookId(bookId) }
                }

                Then("Review list should be updated in the state") {
                    eventually(duration = 1.seconds) {
                        viewModel.withState {
                            isLoading shouldBe false
                            this.reviews.size shouldBe reviews.size
                        }
                    }
                }
            }
        }

        And("is not successful") {
            coEvery { repository.getReviewsByBookId(bookId) }.returns(Either.error(error))

            When("The notes are retrieved with failure") {
                viewModel.getReviews(bookId)

                Then("Review should be retrieved") {
                    coVerify { repository.getReviewsByBookId(bookId) }
                }

                Then("Review list should be updated in the state") {
                    viewModel.withState {
                        this.error shouldBe error.message
                        isLoading shouldBe false
                    }
                }
            }
        }
    }
})
