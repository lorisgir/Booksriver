package com.example.booksriver.data.dto

import com.example.booksriver.data.K
import com.example.booksriver.data.model.Author
import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.IDto
import com.example.booksriver.data.model.UserBook
import com.google.gson.annotations.SerializedName

data class BookDto(
    val id: Int,
    val title: String? = null,
    val authors: List<Author>,
    val averageRating: Float,
    val categories: List<CategoryDto>,
    val description: String? = null,
    val favourite: Boolean,
    @SerializedName("imageUrl")
    val _imageUrl: String? = null,
    val isbn_10: String? = null,
    val isbn_13: String? = null,
    val language: String? = null,
    val pageCount: Int,
    val publishedDate: String? = null,
    val publisher: String? = null,
    val ratingsCount: Int,
    val userBook: UserBookDto? = null
) : IDto {

    private val imageUrl: String?
        get() = _imageUrl?.replace("http://", "https://")

    override fun fromDto(param: Any?): Book {
        val _pageCount = if (pageCount == 0) 100 else pageCount
        return Book(
            id = id,
            title = title ?: "Title",
            authors = authors,
            averageRating = averageRating,
            categories = categories.map { it.fromDto() },
            description = description,
            favourite = favourite,
            imageUrl = imageUrl,
            isbn_10 = isbn_10,
            isbn_13 = isbn_13,
            language = language,
            pageCount = _pageCount,
            publishedDate = publishedDate,
            publisher = publisher,
            ratingsCount = ratingsCount,
            userBook = userBook?.fromDto(_pageCount)
                ?: UserBook(
                    0,
                    K.BookStatus.NOT_STARTED,
                    0f,
                    false
                )
        )
    }

}
