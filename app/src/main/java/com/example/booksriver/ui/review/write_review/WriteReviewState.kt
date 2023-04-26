package com.example.booksriver.ui.review.write_review

import androidx.compose.runtime.mutableStateListOf
import com.example.booksriver.data.model.Review
import com.example.booksriver.view.state.IState

data class WriteReviewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isReviewTextValid: Boolean = true,
    val isReviewScoreValid: Boolean = true,
    val personalReview: Review? = null,
    val score: Double = 0.0,
    val text: String = "",
    val init: Boolean = false
) : IState


