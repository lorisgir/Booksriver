package com.example.booksriver.data.model


data class Profile(
    val id: Int,
    val username: String,
    val follower: Int,
    var following: Int,
    val libraries: List<UserLibrary>,
    val followed: Boolean
) : IModel
