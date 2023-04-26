package com.example.booksriver.data.model

import com.example.booksriver.data.K
import com.example.booksriver.theme.AllColors

data class Book(
    val id: Int,
    val title: String,
    val authors: List<Author>,
    val averageRating: Float,
    val categories: List<Category>,
    val description: String? = null,
    val favourite: Boolean,
    val imageUrl: String? = null,
    val isbn_10: String? = null,
    val isbn_13: String? = null,
    val language: String? = null,
    val pageCount: Int,
    val publishedDate: String? = null,
    val publisher: String? = null,
    val ratingsCount: Int,
    val userBook: UserBook
) : IModel, ISearch {

    override fun equals(other: Any?): Boolean {
        if(other is Book){
            return other.id == id
        }
        return super.equals(other)
    }

    companion object {
        fun build(): Book {
            return Book(
                1,
                "Titolo",
                listOf(Author(1, "Autore")),
                3f,
                listOf(Category(1, "Categoria", AllColors.BLACK, AllColors.WHITE)),
                "Descrizione",
                true,
                "http://books.google.com/books/content?id=P5hWAAAAYAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
                "isb10",
                "sib13",
                "en",
                100,
                "01/01/2020",
                "",
                1,
                UserBook(0, K.BookStatus.READING, 0f, true)
            )
        }
    }


}
