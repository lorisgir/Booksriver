package com.example.booksriver.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.booksriver.base.BooksriverComposableTest
import com.example.booksriver.component.VerticalBookCard
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Author
import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.Category
import com.example.booksriver.data.model.UserBook
import com.example.booksriver.theme.AllColors
import org.junit.Assert.assertEquals
import org.junit.Test

class BookCardTest : BooksriverComposableTest() {

    @Test
    fun testNoteCard() = runTest {
        var isFavourite = true

        val book = Book(
            1,
            "Titolo",
            listOf(Author(1, "Autore")),
            3f,
            listOf(Category(1, "Categoria", AllColors.BLACK, AllColors.WHITE)),
            "Descrizione",
            isFavourite,
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

        setContent {
            VerticalBookCard(book = book, { value, _ -> isFavourite = value }, {}, {_,_ ->})
        }

        val titleNode = onNodeWithText("Titolo")
        val authorNode = onNodeWithText("Autore")
        val categoryNode = onNodeWithText("Categoria")
        val favouriteNote = onNodeWithContentDescription("Favourite Icon")

        titleNode.assertIsDisplayed()
        authorNode.assertIsDisplayed()
        categoryNode.assertIsDisplayed()
        favouriteNote.assertIsDisplayed()

        favouriteNote.performClick()
        favouriteNote.performClick()

        assertEquals(true, isFavourite)
    }
}
