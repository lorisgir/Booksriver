package com.example.booksriver.ui.book

import androidx.compose.runtime.mutableStateListOf
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.UserBook
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.view.state.IState

data class BookState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String? = null,
    val userBook: UserBook = UserBook(0, K.BookStatus.READING, 0f, true),
    val isDialogPagesVisible: Boolean = false,
    val inputPages: String = "",
    val sliderPages: Float = 0f,
    val isValidInputPages: Boolean? = null,
    val bottomSheetScreen: BottomSheetScreen? = null,
    val userLibrariesLoading: Boolean = false,
    val userLibrariesLoading2: Boolean = false,
    val uerLibrariesError: String? = null,
    val userLibraries: List<UserLibrary> = mutableStateListOf(),
    val bookUserLibraries: List<UserLibrary> = mutableStateListOf(),
    val toastMessage: String? = null,
    val isCreateLibraryDialogVisible: Boolean = false,
    val isRefreshing: Boolean = false,
) : IState


sealed class BottomSheetScreen() {
    object StatusBottomSheet : BottomSheetScreen()
    object LibraryBottomSheet : BottomSheetScreen()
}
