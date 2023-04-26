@file:Suppress("UNCHECKED_CAST")

package com.example.booksriver.repository


import com.example.booksriver.data.model.Author
import com.example.booksriver.data.model.Book
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.util.getDefault
import com.example.booksriver.util.getDefaultListFromDto
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthorRepository @Inject internal constructor(
    private val booksriverService: BooksriverService
) {
    suspend fun searchAuthors(q: String): Either<out List<Author>, out ErrorResponse> {
        return runCatching {
            booksriverService.searchAuthors(q).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun getBooksByAuthor(id: Int): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.getBooksByAuthor(id).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun getAuthorById(id: Int): Either<out Author, out ErrorResponse> {
        return runCatching {
            booksriverService.getAuthorById(id).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }
}