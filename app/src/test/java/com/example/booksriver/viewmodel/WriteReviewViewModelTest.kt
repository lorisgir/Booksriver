package com.example.booksriver.viewmodel


import androidx.lifecycle.SavedStateHandle
import com.example.booksriver.data.model.Review
import com.example.booksriver.data.model.User
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.repository.Either
import com.example.booksriver.repository.ReviewRepository
import com.example.booksriver.session.SessionManager
import com.example.booksriver.ui.review.ReviewViewModel
import com.example.booksriver.ui.review.write_review.WriteReviewState
import com.example.booksriver.ui.review.write_review.WriteReviewViewModel
import com.example.booksriver.viewmodel.base.ViewModelBehaviorSpec
import com.example.booksriver.viewmodel.utils.currentStateShouldBe
import com.example.booksriver.viewmodel.utils.withState
import io.kotest.common.ExperimentalKotest
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalKotest::class)
class WriteReviewViewModelTest : ViewModelBehaviorSpec({


    val repository: ReviewRepository = mockk()
    val sessionManager: SessionManager = mockk(relaxUnitFun = true)
    val savedStateHandle = SavedStateHandle().apply {
        set("bookId", "245")
    }

    val reviewId = 1
    val bookId = 245
    val user = User(1, "a", "b", "c")

    val text = "text"
    val score = 3.3
    val review = Review(reviewId, text, bookId, score, user)

    val newText = "textNew"
    val newScore = 4.4
    val newReview = Review(reviewId, newText, bookId, newScore, user)

    val error = ErrorResponse("Bad request", emptyList())

    every { sessionManager.getUser() }.returns(user)

    val viewModel = WriteReviewViewModel(repository, sessionManager, savedStateHandle)
    val reviewViewModel = ReviewViewModel(repository, savedStateHandle)


    Given("The ViewModel") {
        val expectedState = WriteReviewState(
            isLoading = true,
            error = null,
            isReviewTextValid = true,
            isReviewScoreValid = true,
            personalReview = null,
            score = 0.0,
            text = "",
            init = false
        )

        When("Initialized") {
            Then("Initial state should be valid") {
                viewModel currentStateShouldBe expectedState
            }
        }
    }


    Given("A review not yer written") {

        coEvery { repository.getReviewByUserIdAndBookId(user.id, bookId) }.returns(
            Either.error(
                ErrorResponse("Not Found Error")
            )
        )

        When("review is retrieved") {
            viewModel.getPersonalReview(user.id, bookId)

            Then("Review should be get retrieved") {
                coVerify { repository.getReviewByUserIdAndBookId(user.id, bookId) }
            }

            Then("UI state should have validation details") {
                viewModel.withState {
                    this.personalReview shouldBe null
                }
            }
        }
    }

    Given("Review contents") {

        When("When review contents are set") {
            viewModel.onTextChanged(text)
            viewModel.onScoreChanged(score.toFloat())

            Then("UI state should have validation details") {
                viewModel.withState {
                    this.text shouldBe text
                    this.score shouldBe score
                }
            }
        }
    }

    Given("A review for addition") {

        And("Note contents are invalid") {
            viewModel.onTextChanged("")
            viewModel.onScoreChanged(0f)

            When("Review is added") {
                viewModel.onSave(
                    reviewViewModel::onWriteReviewConfirmedSuccess,
                    reviewViewModel::onWriteReviewConfirmedError
                )

                Then("Review states should be invalid") {
                    viewModel.withState {
                        isReviewScoreValid shouldBe false
                        isReviewTextValid shouldBe false
                    }
                }

                Then("Review should NOT be get saved") {
                    coVerify(exactly = 0) {
                        repository.createReview(bookId, score, text)
                    }
                }
            }
        }


        And("Note contents are valid") {
            viewModel.onTextChanged(text)
            viewModel.onScoreChanged(score.toFloat())

            And("Review save is unsuccessful") {
                coEvery {
                    repository.createReview(
                        bookId,
                        score,
                        text
                    )
                }.returns(Either.error(error))

                When("Review is created") {
                    viewModel.onSave(
                        reviewViewModel::onWriteReviewConfirmedSuccess,
                        reviewViewModel::onWriteReviewConfirmedError
                    )

                    Then("Review states should be valid") {
                        viewModel.withState {
                            isReviewScoreValid shouldBe true
                            isReviewTextValid shouldBe true
                        }
                    }

                    Then("Review should be saved") {
                        coVerify { repository.createReview(bookId, score, text) }
                    }

                    Then("Valid UI states should be updated") {
                        viewModel.withState {
                            this.error shouldBe error.message
                        }
                        reviewViewModel.withState {
                            reviews.size shouldBe 0
                        }
                    }
                }
            }

            And("Review save is successful") {
                coEvery { repository.createReview(bookId, score, text) }.returns(
                    Either.success(
                        review
                    )
                )

                When("Review is created") {
                    viewModel.onSave(
                        reviewViewModel::onWriteReviewConfirmedSuccess,
                        reviewViewModel::onWriteReviewConfirmedError
                    )

                    Then("Review states should be valid") {
                        viewModel.withState {
                            isReviewScoreValid shouldBe true
                            isReviewTextValid shouldBe true
                        }
                    }

                    Then("Review should be saved") {
                        coVerify { repository.createReview(bookId, score, text) }
                    }

                    Then("Valid UI states should be updated") {
                        eventually(duration = 1.seconds) {
                            viewModel.withState {
                                isLoading shouldBe false
                                personalReview shouldBe review
                            }
                            reviewViewModel.withState {
                                reviews shouldContain review
                            }
                        }
                    }
                }
            }
        }
    }

    Given("A review for update") {
        viewModel.onTextChanged(newText)
        viewModel.onScoreChanged(newScore.toFloat())

        And("Review save is unsuccessful") {
            coEvery { repository.updateReview(reviewId, newScore, newText) }.returns(
                Either.error(
                    error
                )
            )

            When("Review is updated") {
                viewModel.onSave(
                    reviewViewModel::onWriteReviewConfirmedSuccess,
                    reviewViewModel::onWriteReviewConfirmedError
                )

                Then("Review states should be valid") {
                    viewModel.withState {
                        isReviewScoreValid shouldBe true
                        isReviewTextValid shouldBe true
                    }
                }

                Then("Review should be saved") {
                    coVerify { repository.updateReview(reviewId, newScore, newText) }
                }

                Then("Valid UI states should be updated") {
                    viewModel.withState {
                        this.error shouldBe error.message
                        personalReview shouldBe review
                        personalReview!!.text shouldBe review.text
                    }
                    reviewViewModel.withState {
                        reviews shouldContain review
                    }
                }
            }
        }

        And("Review save is successful") {
            coEvery { repository.updateReview(reviewId, newScore, newText) }.returns(
                Either.success(
                    newReview
                )
            )

            When("Review is updated") {
                viewModel.onSave(
                    reviewViewModel::onWriteReviewConfirmedSuccess,
                    reviewViewModel::onWriteReviewConfirmedError
                )

                Then("Review states should be valid") {
                    viewModel.withState {
                        isReviewScoreValid shouldBe true
                        isReviewTextValid shouldBe true
                    }
                }

                Then("Review should be saved") {
                    coVerify { repository.updateReview(reviewId, newScore, newText) }
                }

                Then("Valid UI states should be updated") {
                    eventually(duration = 1.seconds) {
                        viewModel.withState {
                            isLoading shouldBe false
                            personalReview shouldBe newReview
                            personalReview!!.text shouldBe newReview.text
                        }
                        reviewViewModel.withState {
                            reviews shouldContain review
                        }
                    }
                }
            }
        }
    }

    Given("A review for delete") {

        And("Review deleted is unsuccessful") {
            coEvery { repository.deleteReview(reviewId) }.returns(Either.error(error))

            When("Review is deleted") {
                viewModel.onDelete(
                    reviewViewModel::onWriteReviewConfirmedSuccess,
                    reviewViewModel::onWriteReviewConfirmedError
                )

                Then("Review should be deleted") {
                    coVerify { repository.deleteReview(reviewId) }
                }

                Then("Valid UI states should be updated") {
                    viewModel.withState {
                        this.error shouldBe error.message
                        personalReview shouldBe review
                    }
                    reviewViewModel.withState {
                        reviews shouldContain review
                    }
                }
            }
        }

        And("Review deleted is successful") {
            coEvery { repository.deleteReview(reviewId) }.returns(Either.success(null))

            When("Review is deleted") {
                viewModel.onDelete(
                    reviewViewModel::onWriteReviewConfirmedSuccess,
                    reviewViewModel::onWriteReviewConfirmedError
                )

                Then("Review should be deleted") {
                    coVerify { repository.deleteReview(reviewId) }
                }

                Then("Valid UI states should be updated") {
                    viewModel.withState {
                        personalReview shouldBe null
                    }

                    reviewViewModel.withState {
                        reviews shouldNotContain review
                    }
                }
            }
        }
    }

    Given("A review already written") {
        val altReview = Review(2, "asd", 123, 0.6, user)

        coEvery { repository.getReviewByUserIdAndBookId(user.id, bookId) }.returns(Either.success(altReview))

        When("review is retrieved") {
            viewModel.getPersonalReview(user.id, bookId)

            Then("Review should be get retrieved") {
                coVerify{ repository.getReviewByUserIdAndBookId(user.id, bookId) }
            }

            Then("UI state should have validation details") {
                viewModel.withState {
                    this.personalReview shouldBe altReview
                }
            }
        }
    }
})
