package com.example.booksriver.data.model

data class User(val id: Int, val username: String, val email: String, var token: String? = null) :
    ISearch {

    fun setTokenAndGet(tkn: String): User {
        token = tkn
        return this
    }
}
