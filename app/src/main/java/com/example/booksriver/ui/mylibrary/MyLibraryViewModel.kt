package com.example.booksriver.ui.mylibrary


import androidx.lifecycle.viewModelScope
import com.example.booksriver.repository.BookRepository
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLibraryViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : BaseViewModel<MyLibraryState>(initialState = MyLibraryState()) {

    init {
        refresh()
    }

    fun refresh() {
        reset()
        getUserBooks()
    }

    private fun reset() {
        setState { state -> state.copy(isLoading = true, error = null) }
    }

    private fun getUserBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = bookRepository.getUserBooks()
            response.onSuccess { data ->
                setState { state ->
                    state.copy(
                        isLoading = false,
                        userBooksGrouped = data.groupBy { it.userBook.status }.toSortedMap()
                    )
                }
            }.onFailure { error ->
                setState { state -> state.copy(isLoading = false, error = getDetailsOrMessage(error)) }
            }
        }
    }
}
