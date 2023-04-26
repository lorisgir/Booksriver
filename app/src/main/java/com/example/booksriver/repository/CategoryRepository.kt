@file:Suppress("UNCHECKED_CAST")

package com.example.booksriver.repository


import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.Category
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.util.getDefault
import com.example.booksriver.util.getDefaultFromDto
import com.example.booksriver.util.getDefaultListFromDto
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CategoryRepository @Inject internal constructor(
    private val booksriverService: BooksriverService
) {
    suspend fun getMostPopular(): Either<out List<Category>, out ErrorResponse> {
        return runCatching {
            booksriverService.getMostPopularCategories().getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Category>, out ErrorResponse>
    }

    suspend fun searchCategories(q: String): Either<out List<Category>, out ErrorResponse> {
        return runCatching {
            booksriverService.searchCategories(q).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Category>, out ErrorResponse>
    }

    suspend fun getBooksByCategory(categoryId: Int): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.getBooksByCategory(categoryId).getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun getCategoryById(id: Int): Either<out Category, out ErrorResponse> {
        return runCatching {
            booksriverService.getCategoryById(id).getDefaultFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out Category, out ErrorResponse>
    }
}