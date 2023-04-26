@file:Suppress("UNCHECKED_CAST")

package com.example.booksriver.repository


import com.example.booksriver.data.model.Review
import com.example.booksriver.data.request.CreateReview
import com.example.booksriver.data.request.UpdateReview
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.util.getDefault
import com.example.booksriver.util.getEmpty
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReviewRepository @Inject internal constructor(
    private val booksriverService: BooksriverService
) {

    suspend fun getReviewsByBookId(bookId: Int): Either<out List<Review>, out ErrorResponse> {
        return runCatching {
            booksriverService.getReviewByBookId(bookId).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun getReviewByUserIdAndBookId(
        userId: Int,
        bookId: Int
    ): Either<out Review, out ErrorResponse> {
        return runCatching {
            booksriverService.getReviewByUserIdAndBookId(userId, bookId).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun createReview(
        bookId: Int,
        score: Double,
        text: String
    ): Either<out Review, out ErrorResponse> {
        return runCatching {
            booksriverService.createReview(CreateReview(bookId, score, text)).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun deleteReview(reviewId: Int): Either<out Unit?, out ErrorResponse> {
        return runCatching {
            booksriverService.deleteReview(reviewId).getEmpty()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun updateReview(
        reviewId: Int,
        score: Double,
        text: String
    ): Either<out Review, out ErrorResponse> {
        return runCatching {
            booksriverService.updateReview(reviewId, UpdateReview(score, text)).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }
}
