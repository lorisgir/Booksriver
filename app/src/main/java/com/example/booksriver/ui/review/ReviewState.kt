package com.example.booksriver.ui.review

import androidx.compose.runtime.mutableStateListOf
import com.example.booksriver.data.model.Review
import com.example.booksriver.view.state.IState

data class ReviewState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val reviews: List<Review> = emptyList(),
    val hasPersonalReview: Boolean? = null,
    val toastMessage: String? = null,
    val isRefreshing: Boolean = false
) : IState


