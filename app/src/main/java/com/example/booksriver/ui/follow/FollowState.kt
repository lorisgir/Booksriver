package com.example.booksriver.ui.follow

import androidx.compose.runtime.mutableStateListOf
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Profile
import com.example.booksriver.data.model.User
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.view.state.IState

data class FollowState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val followType: K.ListFollowType? = null,
    val users: List<User> = mutableStateListOf()
) : IState