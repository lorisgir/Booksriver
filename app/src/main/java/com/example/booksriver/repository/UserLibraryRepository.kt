@file:Suppress("UNCHECKED_CAST")

package com.example.booksriver.repository


import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.Category
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.data.request.*
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.util.getDefault
import com.example.booksriver.util.getDefaultFromDto
import com.example.booksriver.util.getDefaultListFromDto
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserLibraryRepository @Inject internal constructor(
    private val booksriverService: BooksriverService
) {
    suspend fun searchUserLibraries(q: String): Either<out List<UserLibrary>, out ErrorResponse> {
        return runCatching {
            booksriverService.searchUserLibraries(q).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<UserLibrary>, out ErrorResponse>
    }

    suspend fun getBooksByUserLibrary(categoryId: Int): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.getBooksByUserLibrary(categoryId).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun getUserLibrariesByUserId(userId: Int): Either<out List<UserLibrary>, out ErrorResponse> {
        return runCatching {
            booksriverService.getUserLibrariesByUserId(userId).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<UserLibrary>, out ErrorResponse>
    }

    suspend fun createUserLibrary(name: String): Either<out UserLibrary, out ErrorResponse> {
        return runCatching {
            booksriverService.createUserLibrary(CreateUserLibraryRequest(name)).getDefaultFromDto()
        }.getOrDefault(Either.error(ErrorResponse()))as Either<out UserLibrary, out ErrorResponse>
    }

    suspend fun editUserLibrary(id: Int, name: String): Either<out UserLibrary, out ErrorResponse> {
        return runCatching {
            booksriverService.editUserLibrary(id, EditUserLibraryRequest(name)).getDefaultFromDto()
        }.getOrDefault(Either.error(ErrorResponse()))as Either<out UserLibrary, out ErrorResponse>
    }

    suspend fun deleteUserLibrary(id: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.deleteUserLibrary(id).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun getUserLibraryById(id: Int): Either<out UserLibrary, out ErrorResponse> {
        return runCatching {
            booksriverService.getUserLibraryById(id).getDefaultFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out UserLibrary, out ErrorResponse>
    }

    suspend fun addBookToUserLibraries(bookId: Int, libraries: List<UserLibrary>): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.addBookToUserLibraries(AddsBook(libraries.map { it.id }, bookId)).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun addBookToUserLibrary(libId: Int, bookId: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.addBookToUserLibrary(libId, AddBookToLibraryRequest(bookId)).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun removeBookFromUserLibrary(libId: Int, bookId: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.removeBookFromUserLibrary(libId, RemoveBookFromLibraryRequest(bookId)).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }
}