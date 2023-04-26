package com.example.booksriver.data.model

import com.example.booksriver.data.K

data class UserBook(
    val pagesRead: Int,
    val status: K.BookStatus,
    val percentage: Float,
    val isAdded: Boolean
) : IModel