package com.example.booksriver.ui.books

import androidx.compose.runtime.mutableStateListOf
import com.example.booksriver.data.model.Book
import com.example.booksriver.view.state.IState

data class BooksState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val listTitle: String? = null,
    val listId: Int? = null,
    val books: List<Book> = mutableStateListOf(),
    val toastMessage: String? = null,
    val isPersonalUserLibrary: Boolean? = null,
    val userLibraryId: Int? = null,
    val isLibraryDeleted: Boolean = false,
    val isEditLibraryDialogVisible: Boolean = false
) : IState


