package com.example.booksriver.ui.profile

import androidx.compose.runtime.mutableStateListOf
import com.example.booksriver.data.model.Profile
import com.example.booksriver.data.model.RemoteList
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.view.state.IState

data class ProfileState(
    val userId: Int = 0,
    val isLogged: Boolean = true,
    val isLoading: Boolean = true,
    val showLibDialog: Boolean = false,
    val toastMessage: String? = null,
    val showUsernameDialog: Boolean = false,
    val newUsername: String = "",
    val isValidUsername: Boolean? = null,
    val error: String? = null,
    val profile: Profile? = null,
    val libraries: List<UserLibrary> = mutableStateListOf(),
    val isRefreshing: Boolean = false,
    val libDialog: UserLibrary? = null,
    val isCreateLibDialog: Boolean = false,
    ) : IState