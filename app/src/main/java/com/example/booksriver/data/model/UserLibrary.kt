package com.example.booksriver.data.model

data class UserLibrary(
    val id: Int,
    val name: String,
    val count: Int? = null,
    val userId: Int,
    val books: List<Book> = emptyList(),
    val color: Int,
    val colorDark: Int
) : IModel, ISearch {
    override fun equals(other: Any?): Boolean {
        if(other is UserLibrary){
            return other.id == id
        }
        return super.equals(other)
    }
}