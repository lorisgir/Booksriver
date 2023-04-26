package com.example.booksriver.ui.mylibrary

import com.example.booksriver.data.K
import com.example.booksriver.data.model.Book
import com.example.booksriver.view.state.IState

data class MyLibraryState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userBooksGrouped: Map<K.BookStatus, List<Book>> = emptyMap(),
    val isRefreshing: Boolean = false,
    ) : IState


