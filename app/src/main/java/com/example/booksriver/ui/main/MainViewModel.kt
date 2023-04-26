package com.example.booksriver.ui.main


import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.data.K.MAIN_CATEGORIES_NO
import com.example.booksriver.data.model.Book
import com.example.booksriver.repository.BookRepository
import com.example.booksriver.repository.CategoryRepository
import com.example.booksriver.repository.UserBookRepository
import com.example.booksriver.session.SessionManager
import com.example.booksriver.theme.AllColors
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val bookRepository: BookRepository,
    private val userBookRepository: UserBookRepository,
    private val categoryRepository: CategoryRepository,
) : BaseViewModel<MainState>(initialState = MainState()) {

    private val _chipData = currentState.chipData.toMutableList()
    private val _currentlyReading = currentState.currentlyReading.copy()
    private val _notStarted = currentState.notStarted.copy()

    init {
        checkUserSession()
        refresh()
    }


    fun refresh() {
        reset()
        getListsData()
        getByStatus(K.BookStatus.READING, _currentlyReading)
    }

    fun onChipClicked(selectedChipIndex: Int) {
        setState { state -> state.copy(selectedChipIndex = selectedChipIndex) }
    }

    fun resetToastMessage() {
        setState { state -> state.copy(toastMessage = null) }
    }

    fun onFavouriteClick(isFavourite: Boolean, bookId: Int) {
        viewModelScope.launch {
            val response =
                if (isFavourite) userBookRepository.addBookToFavourite(bookId) else userBookRepository.removeBookToFavourite(
                    bookId
                )
            response.onFailure { error ->
                setState { state -> state.copy(toastMessage = getDetailsOrMessage(error)) }
            }
        }
    }

    private fun reset() {
        _chipData.forEach {
            it.data.reset()
        }
        _currentlyReading.data.reset()
        _notStarted.data.reset()
        setState { state ->
            state.copy(
                chipData = _chipData.toMutableStateList(),
                currentlyReading = _currentlyReading,
                notStarted = _notStarted
            )
        }
    }

    private fun getByStatus(status: K.BookStatus, listsData: ListsData<Book>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = bookRepository.getUserBooksByStatus(status.status)
            response.onSuccess { data ->
                listsData.data.isLoading = false
                listsData.data.data = data
                if (status == K.BookStatus.READING) {
                    setState { state -> state.copy(currentlyReading = listsData) }
                    if (data.isEmpty()) {
                        setState { state -> state.copy(showReadingOrNotStarted = ShowReadingOrNotStarted.NOT_STARTED) }
                        getByStatus(K.BookStatus.NOT_STARTED, _notStarted)
                    } else {
                        setState { state -> state.copy(showReadingOrNotStarted = ShowReadingOrNotStarted.READING) }
                    }
                } else if (status == K.BookStatus.NOT_STARTED) {
                    setState { state -> state.copy(notStarted = listsData) }
                    if (data.isEmpty()) {
                        setState { state -> state.copy(showReadingOrNotStarted = ShowReadingOrNotStarted.NOTHING) }
                    }
                }

            }.onFailure { error ->
                listsData.data.isLoading = false
                listsData.data.error = getDetailsOrMessage(error)
                if (status == K.BookStatus.READING) {
                    setState { state -> state.copy(currentlyReading = listsData) }
                } else if (status == K.BookStatus.NOT_STARTED) {
                    setState { state -> state.copy(notStarted = listsData) }
                }
            }
        }
    }


    private fun getListsData() {
        viewModelScope.launch(Dispatchers.IO) {
            val categoryResponse = categoryRepository.getMostPopular()
            categoryResponse.onSuccess { list ->
                list.take(MAIN_CATEGORIES_NO).forEach { category ->
                    _chipData.add(
                        ListsData(
                            title = category.name,
                            categoryId = category.id,
                            color = AllColors.getVisibleRandomColor(isDarkMode = false),
                            colorDark = AllColors.getVisibleRandomColor(isDarkMode = true)
                        )
                    )
                }
                setState { state -> state.copy(chipData = _chipData.toMutableStateList()) }
                populateLists()
            }.onFailure {
                populateLists()
            }
        }
    }

    private fun populateLists() {
        _chipData.forEach { item ->
            getBooksFromRepository(item)
        }
    }

    private fun getBooksFromRepository(item: ListsData<Book>) {
        viewModelScope.launch(Dispatchers.IO) {
            val bookResponse = let {
                if (item.categoryId == null) {
                    when (item.title) {
                        "Most Popular" -> bookRepository.getMostPopular()
                        "Suggested" -> bookRepository.getSuggested()
                        else -> bookRepository.getMostPopular()
                    }
                } else {
                    categoryRepository.getBooksByCategory(item.categoryId)
                }
            }

            bookResponse.onSuccess { list ->
                item.data.isLoading = false
                item.data.data = list.take(10)
                setState { state -> state.copy(chipData = _chipData.toMutableStateList()) }
            }.onFailure { error ->
                item.data.isLoading = false
                item.data.error = getDetailsOrMessage(error)
                setState { state -> state.copy(chipData = _chipData.toMutableStateList()) }
            }
        }
    }

    private fun checkUserSession() {
        setState {
            it.copy(
                isUserLoggedIn = sessionManager.getToken() != null, user = sessionManager.getUser()
            )
        }
    }

}
