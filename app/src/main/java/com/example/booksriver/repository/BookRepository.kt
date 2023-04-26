@file:Suppress("UNCHECKED_CAST")

package com.example.booksriver.repository


import android.util.Log
import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.data.request.ChangeUserBookPagesRead
import com.example.booksriver.data.request.ChangeUserBookStatus
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepository @Inject internal constructor(
    private val booksriverService: BooksriverService
) {

    suspend fun getBook(id: Int): Either<out Book, out ErrorResponse> {
        return runCatching {
            booksriverService.getBook(id).getDefaultFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out Book, out ErrorResponse>
    }

    suspend fun getMostPopular(): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.getMostPopularBooks().getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun getSuggested(): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.getSuggestedBooks().getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun getUserBooksByStatus(statusId: Int): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.getUserBooksByStatus(statusId).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun getUserBooks(): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.getUserBooks().getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun searchBooks(q: String): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.searchBooks(q).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun getUserLibrariesFromBookId(id: Int): Either<out List<UserLibrary>, out ErrorResponse> {
        return runCatching {
            booksriverService.getUserLibrariesFromBookId(id).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<UserLibrary>, out ErrorResponse>
    }


}
