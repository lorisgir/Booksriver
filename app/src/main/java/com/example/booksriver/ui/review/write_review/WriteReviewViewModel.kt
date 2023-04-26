package com.example.booksriver.ui.review.write_review


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Review
import com.example.booksriver.repository.ReviewRepository
import com.example.booksriver.session.SessionManager
import com.example.booksriver.util.BooksriverValidators
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class WriteReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<WriteReviewState>(initialState = WriteReviewState()) {

    private var bookId: Int = 0

    init {
        savedStateHandle.get<String>(K.PARAM_BOOK_ID)?.let { bookId ->
            this.bookId = bookId.toInt()
            getPersonalReview(sessionManager.getUser()!!.id, bookId.toInt())
        }
    }

    fun getPersonalReview(userId: Int, bookId: Int) {
        setState { state -> state.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val response = reviewRepository.getReviewByUserIdAndBookId(userId, bookId)
            response.onSuccess { data ->
                setState { state -> state.copy(isLoading = false, personalReview = data, score = data.score, text = data.text, init = true) }
            }.onFailure { error ->
                if (error.message == "Not Found Error") {
                    setState { state -> state.copy(isLoading = false, personalReview = null, init = true) }
                } else {
                    setState { state ->
                        state.copy(
                            isLoading = false,
                            error = getDetailsOrMessage(error)
                        )
                    }
                }
            }
        }
    }

    fun onScoreChanged(score: Float) {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.HALF_DOWN
        setState { state -> state.copy(score = df.format(score).toDouble()) }
    }

    fun onTextChanged(text: String) {
        setState { state -> state.copy(text = text) }
    }

    fun onSave(
        onWriteReviewConfirmedSuccess: ((Int, Review) -> Unit)? = null,
        onWriteReviewConfirmedError: ((String) -> Unit)? = null
    ): Boolean {
        val score = currentState.score
        val text = currentState.text
        val isReviewScoreValid = BooksriverValidators.isValidScore(score)
        val isReviewTextValid = BooksriverValidators.isNotNull(text)

        setState { state ->
            state.copy(
                isReviewTextValid = isReviewTextValid,
                isReviewScoreValid = isReviewScoreValid,
            )
        }

        if (isReviewTextValid && isReviewScoreValid) {
            setState { state -> state.copy(isLoading = true) }

            viewModelScope.launch(Dispatchers.IO) {
                val addOperation = currentState.personalReview == null
                val response = if (addOperation) reviewRepository.createReview(
                    bookId,
                    score,
                    text
                ) else reviewRepository.updateReview(
                    currentState.personalReview!!.id,
                    score,
                    text
                )
                response.onSuccess { review ->
                    setState { state -> state.copy(personalReview = review, isLoading = false) }
                    if (onWriteReviewConfirmedSuccess != null) {
                        onWriteReviewConfirmedSuccess(if (addOperation) 1 else 2, review)
                    }
                }.onFailure { error ->
                    setState { state ->
                        state.copy(
                            error = getDetailsOrMessage(error),
                            isLoading = false
                        )
                    }
                    if (onWriteReviewConfirmedError != null) {
                        onWriteReviewConfirmedError(getDetailsOrMessage(error))
                    }
                }
            }
        }
        return isReviewTextValid
    }

    fun onDelete(
        onWriteReviewConfirmedSuccess: ((Int, Review) -> Unit)? = null,
        onWriteReviewConfirmedError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val review = currentState.personalReview!!.copy()
            val response = reviewRepository.deleteReview(currentState.personalReview!!.id)
            response.onSuccess {
                setState { state -> state.copy(personalReview = null, score = 0.0, text = "") }
                if (onWriteReviewConfirmedSuccess != null) {
                    onWriteReviewConfirmedSuccess(3, review)
                }
            }.onFailure { error ->
                setState { state -> state.copy(error = getDetailsOrMessage(error)) }
                if (onWriteReviewConfirmedError != null) {
                    onWriteReviewConfirmedError(getDetailsOrMessage(error))
                }
            }
        }
    }

}
