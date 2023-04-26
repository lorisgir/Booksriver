package com.example.booksriver.data.response

data class ErrorResponse(
    val message: String = "Something went wrong!",
    val details: List<String> = emptyList()
) {
}