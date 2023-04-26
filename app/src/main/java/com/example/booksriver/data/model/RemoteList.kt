package com.example.booksriver.data.model

data class RemoteList<T>(
    var isLoading: Boolean = true,
    var data: List<T> = emptyList(),
    var error: String? = null
) {
    fun reset() {
        error = null
        isLoading = true
        data = emptyList()
    }
}