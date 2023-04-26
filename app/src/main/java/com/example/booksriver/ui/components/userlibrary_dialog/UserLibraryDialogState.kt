package com.example.booksriver.ui.components.userlibrary_dialog

import com.example.booksriver.view.state.IState

data class UserLibraryDialogState(
    val dialogHasError: Boolean = false,
    val newLibraryName: String = "",
    val libraryId: Int? = null,
    val error: String? = null,
    val isValidLibName: Boolean? = null,
    ) : IState