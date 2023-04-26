package com.example.booksriver.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.res.stringResource
import com.example.booksriver.R
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.RemoteList
import com.example.booksriver.data.model.User
import com.example.booksriver.theme.AllColors
import com.example.booksriver.view.state.IState

data class MainState(
    val isUserLoggedIn: Boolean? = null,
    val user: User? = null,
    val myBook: List<Book> = emptyList(),
    val chipData: List<ListsData<Book>> = mutableStateListOf(
        ListsData(
            titleId = R.string.best_seller,
            color = AllColors.getVisibleRandomColor(isDarkMode = false),
            colorDark = AllColors.getVisibleRandomColor(isDarkMode = true)
        ),
        ListsData(
            titleId = R.string.suggested,
            color = AllColors.getVisibleRandomColor(isDarkMode = false),
            colorDark = AllColors.getVisibleRandomColor(isDarkMode = true)
        )
    ),
    val currentlyReading: ListsData<Book> = ListsData(
        titleId = K.BookStatus.READING.titleId,
        statusId = K.BookStatus.READING.status
    ),
    val notStarted: ListsData<Book> = ListsData(
        titleId = K.BookStatus.NOT_STARTED.titleId,
        statusId = K.BookStatus.NOT_STARTED.status
    ),
    val selectedChipIndex: Int = 0,
    val isRefreshing: Boolean = false,
    val showReadingOrNotStarted: ShowReadingOrNotStarted = ShowReadingOrNotStarted.READING,
    val toastMessage: String? = null
) : IState

enum class ShowReadingOrNotStarted{
    READING,
    NOT_STARTED,
    NOTHING
}

data class ListsData<T>(
    val titleId: Int? = null,
    val title: String = "",
    val color: Int? = null,
    val colorDark: Int? = null,
    val categoryId: Int? = null,
    val statusId: Int? = null,
    var data: RemoteList<T> = RemoteList()
)