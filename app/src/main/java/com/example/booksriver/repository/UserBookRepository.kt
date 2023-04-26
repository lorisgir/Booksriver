@file:Suppress("UNCHECKED_CAST")

package com.example.booksriver.repository


import com.example.booksriver.data.request.ChangeUserBookPagesRead
import com.example.booksriver.data.request.ChangeUserBookStatus
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.util.getDefault
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserBookRepository @Inject internal constructor(
    private val booksriverService: BooksriverService
) {

    suspend fun changeUserBookStatus(
        bookId: Int,
        statusId: Int
    ): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.changeUserBookStatus(bookId, ChangeUserBookStatus(statusId))
                .getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun addReadingUserBook(bookId: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.addReadingUserBook(bookId).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun changeUserBookPagesRead(
        bookId: Int,
        pagesRead: Int
    ): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.changeUserBookPagesRead(bookId, ChangeUserBookPagesRead(pagesRead))
                .getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun removeBook(bookId: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.removeBook(bookId).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun addBookToFavourite(bookId: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.addBookToFavourite(bookId).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun removeBookToFavourite(bookId: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.removeBookToFavourite(bookId).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }
}
