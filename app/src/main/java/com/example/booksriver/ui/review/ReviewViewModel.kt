package com.example.booksriver.ui.review


import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Review
import com.example.booksriver.repository.ReviewRepository
import com.example.booksriver.session.SessionManager
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ReviewState>(initialState = ReviewState()) {

    private val _reviews = currentState.reviews.toMutableList()
    private var bookId: Int = 0

    init {
        savedStateHandle.get<String>(K.PARAM_BOOK_ID)?.let { bookId ->
            this.bookId = bookId.toInt()
            refresh()
        }
    }

    fun refresh() {
        getReviews(bookId)
    }

    fun resetToastMessage() {
        setState { state -> state.copy(toastMessage = null) }
    }

    fun getReviews(bookId: Int) {
        _reviews.clear()
        viewModelScope.launch(Dispatchers.IO) {
            val response = reviewRepository.getReviewsByBookId(bookId)
            response.onSuccess { data ->
                _reviews.addAll(data)

                setState { state ->
                    state.copy(isLoading = false, reviews = _reviews.toImmutableList())
                }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        isLoading = false,
                        error = getDetailsOrMessage(error)
                    )
                }
            }
        }
    }

    fun onWriteReviewInit(hasPersonalReview: Boolean){
        setState { state -> state.copy(hasPersonalReview = hasPersonalReview) }
    }

    fun onWriteReviewConfirmed() {
        setState { state -> state.copy(isLoading = true) }
    }

    fun onWriteReviewConfirmedSuccess(operationType: Int, review: Review) {
        when (operationType) {
            1 -> _reviews.add(review)
            2 -> _reviews[_reviews.indexOf(review)] = review
            3 -> _reviews.remove(review)
        }
        setState { state ->
            state.copy(
                reviews = _reviews.toMutableStateList(),
                hasPersonalReview = operationType != 3,
                isLoading = false
            )
        }
    }

    fun onWriteReviewConfirmedError(error: String) {
        setState { state -> state.copy(toastMessage = error,  isLoading = false) }
    }
}
