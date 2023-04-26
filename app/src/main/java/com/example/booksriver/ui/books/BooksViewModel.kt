package com.example.booksriver.ui.books


import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.repository.*
import com.example.booksriver.session.SessionManager
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val userBookRepository: UserBookRepository,
    private val userRepository: UserRepository,
    private val authorRepository: AuthorRepository,
    private val categoryRepository: CategoryRepository,
    private val userLibraryRepository: UserLibraryRepository,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<BooksState>(initialState = BooksState()) {

    private var _listId = 0
    private var _listType = ""

    init {
        val listId = savedStateHandle.get<String>(K.PARAM_LIST_ID)
        val listType = savedStateHandle.get<String>(K.PARAM_LIST_TYPE)
        if (listType != null && listId != null) {
            _listId = listId.toInt()
            _listType = listType
            getTitle(_listId, _listType)
        }
    }

    fun refresh(){
        loadBooks(_listId, _listType)
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

    fun toggleEditLibraryNameDialog() {
        setState { state -> state.copy(isEditLibraryDialogVisible = !currentState.isEditLibraryDialogVisible) }
    }

    fun onUserLibraryNameEdited(userLibrary: UserLibrary) {
        setState { state ->
            state.copy(
                listTitle = userLibrary.name,
                isEditLibraryDialogVisible = false
            )
        }
    }

    fun onUserLibraryNameEditedError(errorMessage: String) {
        setState { state -> state.copy(toastMessage = errorMessage) }
    }

    fun deleteUserLibrary() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userLibraryRepository.deleteUserLibrary(currentState.userLibraryId!!)
            response.onSuccess {
                setState { state ->
                    state.copy(
                        isLibraryDeleted = true,
                        toastMessage = "User Library Deleted Successfully"
                    )
                }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        toastMessage = getDetailsOrMessage(error)
                    )
                }
            }
        }
    }

    private fun getTitle(id: Int, listType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (listType) {
                K.ListBookType.AUTHOR.type -> authorRepository.getAuthorById(id)
                    .onSuccess { data -> setState { state -> state.copy(listTitle = data.name) } }
                K.ListBookType.CATEGORY.type -> categoryRepository.getCategoryById(id)
                    .onSuccess { data -> setState { state -> state.copy(listTitle = data.name) } }
                K.ListBookType.USER_LIBRARY.type -> {
                    userLibraryRepository.getUserLibraryById(id)
                        .onSuccess { userLibrary ->
                            setState { state ->
                                state.copy(
                                    listTitle = userLibrary.name,
                                    isPersonalUserLibrary = sessionManager.getUser()!!.id == userLibrary.userId,
                                    userLibraryId = userLibrary.id
                                )
                            }
                        }
                }
                K.ListBookType.FAVOURITE.type -> setState { state -> state.copy(listTitle = "Favourites") }
                else -> throw java.lang.IllegalArgumentException("list type not supported")
            }
        }
    }


    private fun loadBooks(listId: Int, listType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = when (listType) {
                K.ListBookType.AUTHOR.type -> authorRepository.getBooksByAuthor(listId)
                K.ListBookType.CATEGORY.type -> categoryRepository.getBooksByCategory(listId)
                K.ListBookType.USER_LIBRARY.type -> userLibraryRepository.getBooksByUserLibrary(
                    listId
                )
                K.ListBookType.FAVOURITE.type -> userRepository.getBooksByUserFavourites()
                else -> throw java.lang.IllegalArgumentException("list type not supported")
            }
            response.onSuccess { data ->
                setState { state -> state.copy(isLoading = false, books = data.sortedBy { it.title }, listId = listId) }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        isLoading = false,
                        error = getDetailsOrMessage(error)
                    )
                }
            }
        }
    }
}
