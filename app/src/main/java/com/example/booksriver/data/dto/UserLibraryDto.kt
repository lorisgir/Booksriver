package com.example.booksriver.data.dto

import com.example.booksriver.data.model.IDto
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.theme.AllColors

data class UserLibraryDto(
    val id: Int,
    val name: String,
    val count: Int? = null,
    val userId: Int,
    val books: List<BookDto>? = emptyList()
) : IDto {
    override fun fromDto(param: Any?): UserLibrary {
        return UserLibrary(
            id = id,
            name = name,
            userId = userId,
            count = count ?: 0,
            books = if (books.isNullOrEmpty()) emptyList() else books.map { it.fromDto() },
            color = AllColors.getVisibleRandomColor(isDarkMode = false),
            colorDark = AllColors.getVisibleRandomColor(isDarkMode = true),
        )
    }
}