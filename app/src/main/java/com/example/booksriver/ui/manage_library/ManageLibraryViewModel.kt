package com.example.booksriver.ui.manage_library


import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Book
import com.example.booksriver.repository.BookRepository
import com.example.booksriver.repository.UserLibraryRepository
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ManageLibraryViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val userLibraryRepository: UserLibraryRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ManageLibraryState>(initialState = ManageLibraryState()) {

    private var _libraryId: Int = 0
    private var _libraryBooks = currentState.libraryBooks.toMutableList()
    private val _searchedData = currentState.searchedData.toMutableList()
    private val _text = MutableStateFlow("")

    init {
        val libraryId = savedStateHandle.get<String>(K.PARAM_LIB_ID)

        if (libraryId != null) {
            _libraryId = libraryId.toInt()
        }

        getUserLibraryBooks()

        viewModelScope.launch {
            _text.debounce(500)
                .collect(::search)
        }
    }

    fun resetToastMessage() {
        setState { state -> state.copy(toastMessage = null) }
    }

    fun onSearchTextChanged(searchText: String) {
        _text.value = searchText
        if (searchText.trim().isNotEmpty()) {
            setState { state ->
                state.copy(
                    isLoadingTabAdd = true,
                    isSearchTextValid = true,
                    hasSearchedOnce = true,
                    searchText = searchText
                )
            }
        } else {
            _searchedData.clear()
            setState { state ->
                state.copy(
                    isLoadingTabAdd = false,
                    isSearchTextValid = false,
                    searchText = searchText,
                    searchedData = _searchedData.toMutableStateList()
                )
            }
        }

    }

    private fun getUserLibraryBooks() {
        setState { state -> state.copy(isLoadingTabDelete = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val response = userLibraryRepository.getBooksByUserLibrary(_libraryId)
            response.onSuccess { data ->
                _libraryBooks.addAll(data.map { AnimatedItem(book = it) })
                setState { state ->
                    state.copy(
                        libraryBooks = _libraryBooks.toMutableStateList(),
                        isLoadingTabDelete = false
                    )
                }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        isLoadingTabDelete = false,
                        errorTabDelete = getDetailsOrMessage(error)
                    )
                }
            }
        }
    }

    private fun search(searchText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = bookRepository.searchBooks(searchText)
            response.onSuccess { data ->
                _searchedData.clear()

                // Check if book is already present in lib before inserting it
                data.forEach { book ->
                    if (!_libraryBooks.containss(book)) {
                        _searchedData.add(AnimatedItem(book = book))
                    }
                    if (_searchedData.size >= 10) {
                        return@forEach
                    }
                }

                setState { state ->
                    state.copy(
                        searchedData = _searchedData.toMutableStateList(),
                        isLoadingTabAdd = false
                    )
                }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        isLoadingTabAdd = false,
                        errorTabAdd = getDetailsOrMessage(error)
                    )
                }
            }
        }
    }


    fun addBookToLib(item: AnimatedItem<Book>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_libraryBooks.containss(item)) {
                setState { state -> state.copy(toastMessage = "Already Present!") }
                return@launch
            }

            val response = userLibraryRepository.addBookToUserLibrary(_libraryId, item.book.id)
            response.onSuccess {
                _libraryBooks.adds(item)

                val index = _searchedData.indexOfFirst { it.book == item.book }
                _searchedData[index].visible.targetState = false
                setState { state ->
                    state.copy(
                        searchedData = _searchedData.toMutableStateList(),
                        libraryBooks = _libraryBooks.toMutableStateList()
                    )
                }
            }.onFailure { error ->
                setState { state -> state.copy(toastMessage = getDetailsOrMessage(error)) }
            }
        }
    }

    fun removeBookFromLib(item: AnimatedItem<Book>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_libraryBooks.containss(item)) {
                setState { state -> state.copy(toastMessage = "Not Present!") }
                return@launch
            }

            val response = userLibraryRepository.removeBookFromUserLibrary(_libraryId, item.book.id)
            response.onSuccess {
                val index = _libraryBooks.indexOfFirst { it.book == item.book }
                _libraryBooks[index].visible.targetState = false
                setState { state -> state.copy(libraryBooks = _libraryBooks.toMutableStateList()) }
            }.onFailure { error ->
                setState { state -> state.copy(toastMessage = getDetailsOrMessage(error)) }
            }
        }
    }

    fun List<AnimatedItem<Book>>.containss(item: AnimatedItem<Book>): Boolean {
        return if (!contains(item)) {
            false
        } else {
            first { it.book == item.book }.visible.targetState
        }
    }

    fun List<AnimatedItem<Book>>.containss(item: Book): Boolean {
        return if (_libraryBooks.firstOrNull { it.book == item } == null) {
            false
        } else {
            first { it.book == item }.visible.targetState
        }
    }

    fun MutableList<AnimatedItem<Book>>.adds(item: AnimatedItem<Book>) {
        val index = indexOfFirst { it.book == item.book }
        if (index >= 0) {
            get(index).visible.targetState = true
        } else {
            add(AnimatedItem(book = item.book))
        }
    }


}

