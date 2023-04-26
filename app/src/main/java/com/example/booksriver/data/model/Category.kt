package com.example.booksriver.data.model

data class Category(
    val id: Int,
    val name: String,
    val color: Int,
    val colorDark: Int,
) : IModel, ISearch