package com.example.booksriver.data.model

data class Review(
    val id: Int,
    val text: String,
    val bookId: Int,
    val score: Double,
    val user: User
) {
    companion object {
        fun build(): Review {
            return Review(1, "dsdsa dsad sadas ", 1, 2.2, User(1, "username", "email"))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Review) {
            return other.id == id
        }
        return super.equals(other)
    }
}