package com.example.booksriver.data.model

interface IDto {
    fun fromDto(param: Any? = null): IModel
}