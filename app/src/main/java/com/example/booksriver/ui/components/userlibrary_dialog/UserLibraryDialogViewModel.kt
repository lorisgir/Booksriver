package com.example.booksriver.ui.components.userlibrary_dialog

import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.repository.UserLibraryRepository
import com.example.booksriver.util.BooksriverValidators
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLibraryDialogViewModel @Inject constructor(
    private val userLibraryRepository: UserLibraryRepository
) : BaseViewModel<UserLibraryDialogState>(initialState = UserLibraryDialogState()) {

    fun setLibrary(id: Int, name: String) {
        setState { state -> state.copy(libraryId = id, newLibraryName = name) }
    }

    fun setLibraryName(name: String) {
        setState { state -> state.copy(newLibraryName = name) }
    }

    fun saveLibrary(
        onConfirmed: suspend (UserLibrary) -> Unit,
        onConfirmedError: suspend (String) -> Unit,
        isCreate: Boolean
    ) {
        val newLibraryName = currentState.newLibraryName
        val isValid = BooksriverValidators.isNotNull(newLibraryName)
        setState { state -> state.copy(isValidLibName = isValid) }

        if (isValid) {
            viewModelScope.launch(Dispatchers.IO) {
                val response =
                    if (isCreate) userLibraryRepository.createUserLibrary(currentState.newLibraryName) else userLibraryRepository.editUserLibrary(
                        currentState.libraryId!!,
                        currentState.newLibraryName
                    )
                response.onSuccess { userLibrary ->
                    setState { state -> state.copy(newLibraryName = "") }
                    onConfirmed(userLibrary)
                }.onFailure { error ->
                    onConfirmedError(
                        getDetailsOrMessage(error)
                    )
                }
            }
        }
    }
}