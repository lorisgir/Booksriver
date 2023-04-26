package com.example.booksriver.data.dto

import android.util.Log
import com.example.booksriver.data.Singleton
import com.example.booksriver.data.model.Category
import com.example.booksriver.data.model.IDto
import com.example.booksriver.theme.AllColors

data class CategoryDto(
    val id: Int,
    val name: String,
) : IDto {

    override fun fromDto(param: Any?): Category {
        return Category(
            id = id,
            name = name,
            color = AllColors.getVisibleRandomColor(isDarkMode = false),
            colorDark =  AllColors.getVisibleRandomColor(isDarkMode = true),
        )
    }
}
