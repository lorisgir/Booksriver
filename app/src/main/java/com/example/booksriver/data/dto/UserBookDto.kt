package com.example.booksriver.data.dto

import com.example.booksriver.data.K
import com.example.booksriver.data.model.IDto
import com.example.booksriver.data.model.UserBook
import com.example.booksriver.util.calculatePercentage

data class UserBookDto(
    val pagesRead: Int,
    val status: Int
) : IDto {
    override fun fromDto(param: Any?): UserBook {
        val totalPages = if (param != null) param as Int else 0
        val _status = K.BookStatus.values().first { it.status == status }
        val percentage =
            when (_status) {
                K.BookStatus.FINISHED -> 1f
                K.BookStatus.NOT_STARTED -> 0f
                else -> calculatePercentage(pagesRead, totalPages)
            }
        return UserBook(pagesRead = pagesRead, status = _status, percentage = percentage, isAdded = true)
    }
}