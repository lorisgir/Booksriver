package com.example.booksriver.ui.book

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.repository.BookRepository
import com.example.booksriver.repository.UserBookRepository
import com.example.booksriver.repository.UserLibraryRepository
import com.example.booksriver.session.SessionManager
import com.example.booksriver.util.BooksriverValidators
import com.example.booksriver.util.calculatePercentage
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val userBookRepository: UserBookRepository,
    private val userLibraryRepository: UserLibraryRepository,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<BookState>(initialState = BookState()) {

    val _userLibraries = currentState.userLibraries.toMutableList()
    val _bookUserLibraries = currentState.bookUserLibraries.toMutableList()
    var bookId: Int = 0

    init {
        savedStateHandle.get<String>(K.PARAM_BOOK_ID)?.let { bookId ->
            this.bookId = bookId.toInt()
            refresh()
        }
    }

    fun refresh() {
        _userLibraries.clear()
        _bookUserLibraries.clear()
        loadBook(bookId)
        loadBookUserLibraries(bookId)
        loadUserLibraries()
    }

    fun resetToastMessage() {
        setState { state -> state.copy(toastMessage = null) }
    }

    fun toggleCreateLibraryDialog() {
        setState { state -> state.copy(isCreateLibraryDialogVisible = !currentState.isCreateLibraryDialogVisible) }
    }

    fun onUserLibraryCreated(userLibrary: UserLibrary) {
        _userLibraries.add(userLibrary)
        setState { state ->
            state.copy(
                userLibraries = _userLibraries.toMutableStateList(),
                isCreateLibraryDialogVisible = false
            )
        }
    }

    fun onUserLibraryCreatedError(errorMessage: String) {
        setState { state -> state.copy(toastMessage = errorMessage) }
    }

    fun onUserLibraryClicked(userLibrary: UserLibrary, toAdd: Boolean) {
        viewModelScope.launch {
            if (toAdd) {
                _bookUserLibraries.add(userLibrary)
            } else {
                _bookUserLibraries.remove(userLibrary)
            }
            setState { state -> state.copy(bookUserLibraries = _bookUserLibraries.toMutableStateList()) }

            val response = userLibraryRepository.addBookToUserLibraries(
                currentState.book!!.id,
                _bookUserLibraries
            )

            response.onFailure { error ->
                if (!toAdd) {
                    _bookUserLibraries.add(userLibrary)
                } else {
                    _bookUserLibraries.remove(userLibrary)
                }
                setState { state ->
                    state.copy(
                        bookUserLibraries = _bookUserLibraries.toMutableStateList(),
                        toastMessage = getDetailsOrMessage(error)
                    )
                }
            }
        }
    }

    fun onBottomSheetChanged(bottomSheetScreen: BottomSheetScreen?) {
        setState { state -> state.copy(bottomSheetScreen = bottomSheetScreen) }
    }

    fun onReadingStatusChanged(status: K.BookStatus) {
        updateUserBookStatus(status)
    }

    fun onSliderPagesChanged(sliderPages: Float) {
        updateUserBookPages(sliderPages.toInt())
    }

    fun onSliderPagesEndDrag() {
        updateRemoteUserBookPages()
    }

    fun onInputPagesChanged(inputPages: String) {
        setState { state -> state.copy(inputPages = inputPages) }
    }

    fun onDialogPagesVisible() {
        setState { state ->
            state.copy(
                isDialogPagesVisible = true,
                inputPages = currentState.userBook.pagesRead.toString()
            )
        }
    }

    fun onDialogPagesDismissed() {
        setState { state -> state.copy(isDialogPagesVisible = false) }
    }

    fun onDialogPagesConfirmed() {
        val inputPages = currentState.inputPages
        val isValidInputPages = BooksriverValidators.isNumber(inputPages)
        setState { state -> state.copy(isValidInputPages = isValidInputPages) }

        if (isValidInputPages) {
            updateUserBookPages(inputPages.toInt())
            updateRemoteUserBookPages()
            setState { state ->
                state.copy(isDialogPagesVisible = false)
            }
        }
    }

    fun onFavouriteClick(isFavourite: Boolean) {
        viewModelScope.launch {
            val response =
                if (isFavourite) userBookRepository.addBookToFavourite(currentState.book!!.id) else userBookRepository.removeBookToFavourite(
                    currentState.book!!.id
                )
            response.onSuccess {
                setState { state ->
                    state.copy(book = currentState.book!!.copy(favourite = isFavourite))
                }
            }.onFailure { error ->
                setState { state -> state.copy(toastMessage = getDetailsOrMessage(error)) }
            }
        }
    }

    fun addReadingUserBook() {
        viewModelScope.launch {
            val response = userBookRepository.addReadingUserBook(currentState.book!!.id)
            response.onSuccess {
                setState { state ->
                    state.copy(
                        userBook = currentState.userBook.copy(
                            status = K.BookStatus.READING,
                            isAdded = true
                        )
                    )
                }
            }.onFailure { error ->
                setState { state -> state.copy(toastMessage = getDetailsOrMessage(error)) }
            }
        }
    }

    fun removeBook() {
        viewModelScope.launch {
            val response = userBookRepository.removeBook(currentState.book!!.id)
            response.onSuccess {
                setState { state ->
                    state.copy(
                        userBook = currentState.userBook.copy(
                            status = K.BookStatus.NOT_STARTED,
                            pagesRead = 0,
                            percentage = 0f,
                            isAdded = false
                        )
                    )
                }
            }.onFailure { error ->
                setState { state -> state.copy(toastMessage = getDetailsOrMessage(error)) }
            }
        }
    }

    private fun updateUserBookStatus(status: K.BookStatus) {
        viewModelScope.launch {

            val response =
                userBookRepository.changeUserBookStatus(currentState.book!!.id, status.status)

            response.onSuccess {
                val userBook = when (status) {
                    K.BookStatus.NOT_STARTED -> currentState.userBook.copy(
                        pagesRead = 0,
                        percentage = 0f,
                        status = status
                    )
                    K.BookStatus.FINISHED -> currentState.userBook.copy(
                        pagesRead = currentState.book!!.pageCount,
                        percentage = 1f,
                        status = status
                    )
                    K.BookStatus.READING, K.BookStatus.DROPPED -> currentState.userBook.copy(status = status)
                }
                setState { state ->
                    state.copy(
                        userBook = userBook,
                        sliderPages = userBook.pagesRead.toFloat()
                    )
                }
            }.onFailure { error ->
                setState { state -> state.copy(toastMessage = getDetailsOrMessage(error)) }
            }

        }

    }

    private fun updateRemoteUserBookPages() {
        viewModelScope.launch {
            val response = userBookRepository.changeUserBookPagesRead(
                currentState.book!!.id,
                currentState.userBook.pagesRead
            )
            response.onFailure { error ->
                setState { state -> state.copy(toastMessage = getDetailsOrMessage(error)) }
            }
        }
    }

    private fun updateUserBookPages(pages: Int) {
        val _pages = pages.coerceIn(0, currentState.book!!.pageCount)
        val status = when (_pages) {
            0 -> K.BookStatus.NOT_STARTED
            in 1 until currentState.book!!.pageCount -> K.BookStatus.READING
            else -> K.BookStatus.FINISHED
        }
        setState { state ->
            state.copy(
                sliderPages = _pages.toFloat(),
                userBook = currentState.userBook.copy(
                    pagesRead = _pages,
                    percentage = calculatePercentage(_pages, currentState.book!!.pageCount),
                    status = status
                )
            )
        }

    }

    private fun loadUserLibraries() {
        viewModelScope.launch {
            setState { state -> state.copy(userLibrariesLoading = true) }
            val response =
                userLibraryRepository.getUserLibrariesByUserId(sessionManager.getUser()!!.id)

            response.onSuccess { data ->
                _userLibraries.addAll(data)
                setState { state ->
                    state.copy(
                        userLibrariesLoading = false,
                        userLibraries = _userLibraries.toMutableStateList()
                    )
                }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        userLibrariesLoading = false,
                        uerLibrariesError = getDetailsOrMessage(error)
                    )
                }
            }
        }
    }

    private fun loadBookUserLibraries(id: Int) {
        viewModelScope.launch {
            setState { state -> state.copy(userLibrariesLoading2 = true) }
            val response =
                bookRepository.getUserLibrariesFromBookId(id)

            response.onSuccess { data ->
                _bookUserLibraries.addAll(data)
                setState { state ->
                    state.copy(
                        userLibrariesLoading2 = false,
                        bookUserLibraries = _bookUserLibraries.toMutableStateList()
                    )
                }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        userLibrariesLoading2 = false,
                        uerLibrariesError = getDetailsOrMessage(error)
                    )
                }
            }
        }
    }

    private fun loadBook(bookId: Int) {
        viewModelScope.launch {
            setState { state -> state.copy(isLoading = true) }
            val response = bookRepository.getBook(bookId)

            response.onSuccess { book ->
                setState { state ->
                    state.copy(
                        isLoading = false,
                        book = book,
                        userBook = book.userBook,
                        sliderPages = book.userBook.pagesRead.toFloat()
                    )
                }
            }.onFailure { error ->
                setState { state ->
                    state.copy(isLoading = false, error = getDetailsOrMessage(error))
                }
            }
        }
    }
}
