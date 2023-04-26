package com.example.booksriver.ui.manage_library

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.mutableStateListOf
import com.example.booksriver.R
import com.example.booksriver.data.model.Book
import com.example.booksriver.view.state.IState

data class ManageLibraryState(
    val searchText: String = "",
    val isSearchTextValid: Boolean? = null,
    val hasSearchedOnce: Boolean = false,
    val isLoadingTabAdd: Boolean = true,
    val errorTabAdd: String? = null,
    val searchedData: List<AnimatedItem<Book>> = mutableStateListOf(),

    val isLoadingTabDelete: Boolean = true,
    val errorTabDelete: String? = null,
    val libraryBooks: List<AnimatedItem<Book>> = mutableStateListOf(),

    val toastMessage: String? = null,
    val tabs: List<Int> = listOf(R.string.add_to_library, R.string.remove_books)
) : IState

data class AnimatedItem<E>(
    var visible: MutableTransitionState<Boolean> = MutableTransitionState(
        false
    ).apply { targetState = true }, val book: E
)