package com.example.booksriver.ui.profile

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.repository.UserLibraryRepository
import com.example.booksriver.repository.UserRepository
import com.example.booksriver.session.SessionManager
import com.example.booksriver.util.BooksriverValidators
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userLibraryRepository: UserLibraryRepository,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ProfileState>(initialState = ProfileState()) {

    private val _libraries = currentState.libraries.toMutableList()

    init {
        savedStateHandle.get<String>(K.PARAM_USER_ID)?.let { userId ->
            setState { state ->
                state.copy(
                    userId = userId.toInt()
                )
            }
        }

        refresh()
    }

    fun refresh() {
        _libraries.clear()
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                if (state.value.userId == 0) userRepository.getLoggedUser() else userRepository.getUser(
                    state.value.userId
                )
            response.onSuccess { data ->
                _libraries.addAll(data.libraries)
                setState { state ->
                    state.copy(
                        isLoading = false,
                        profile = data,
                        libraries = _libraries.toMutableStateList(),
                    )
                }
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

    fun resetToastMessage() {
        setState { state -> state.copy(toastMessage = null) }
    }

    fun setUsername(name: String) {
        setState { state -> state.copy(newUsername = name) }
    }

    fun changeUsername() {
        val name = currentState.newUsername
        val isValid = BooksriverValidators.isNotNull(name)
        setState { state -> state.copy(isValidUsername = isValid) }

        if (isValid) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = userRepository.changeUsername(currentState.newUsername)
                response.onSuccess { data ->
                    setState { state ->
                        state.copy(
                            profile = currentState.profile!!.copy(username = data.username),
                            showUsernameDialog = false
                        )
                    }
                }.onFailure { error ->
                    setState { state ->
                        state.copy(
                            toastMessage = getDetailsOrMessage(error),
                            showUsernameDialog = false
                        )
                    }
                }
            }
        }
    }

    fun showUsernameDialog() {
        setState { state ->
            state.copy(
                showUsernameDialog = true,
                newUsername = currentState.profile!!.username
            )
        }
    }

    fun hideDialog() {
        setState { state ->
            state.copy(
                showLibDialog = false,
                showUsernameDialog = false,
                isValidUsername = true,
                newUsername = "",
            )
        }
    }

    fun showCreateLibDialog() {
        setState { state ->
            state.copy(
                showLibDialog = true,
                isCreateLibDialog = true,
                libDialog = null
            )
        }
    }

    fun showEditLibDialog(lib: UserLibrary) {
        setState { state ->
            state.copy(
                showLibDialog = true,
                isCreateLibDialog = false,
                libDialog = lib
            )
        }
    }

    fun toggleCreateLibraryDialog() {
        setState { state -> state.copy(showLibDialog = !currentState.showLibDialog) }
    }

    fun onUserLibraryCreated(userLibrary: UserLibrary) {
        if (currentState.isCreateLibDialog) {
            _libraries.add(userLibrary)

        } else {
            _libraries[_libraries.indexOf(userLibrary)] = userLibrary
        }
        setState { state ->
            state.copy(
                libraries = _libraries.toMutableStateList(),
                showLibDialog = false
            )
        }
    }

    fun onUserLibraryCreatedError(errorMessage: String) {
        setState { state -> state.copy(toastMessage = errorMessage) }
    }


    fun logout() {
        sessionManager.clearSession()

        setState { state ->
            state.copy(
                isLogged = false
            )
        }
    }

    fun deleteLibrary(userLibrary: UserLibrary) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userLibraryRepository.deleteUserLibrary(userLibrary.id)
            response.onSuccess {
                _libraries.removeAt(_libraries.indexOf(userLibrary))
                setState { state ->
                    state.copy(
                        libraries = _libraries.toMutableStateList(),
                        showLibDialog = false
                    )
                }
            }.onFailure { error ->
                setState { state ->
                    state.copy(
                        toastMessage = getDetailsOrMessage(error),
                        showLibDialog = false
                    )
                }
            }
        }
    }

    fun handleFollow() {
        if (state.value.profile!!.followed) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = userRepository.unfollowUser(state.value.profile!!.id)
                response.onSuccess {
                    setState { state ->
                        state.copy(
                            profile = state.profile!!.copy(
                                followed = false,
                                follower = state.profile.follower - 1
                            )
                        )
                    }
                }.onFailure { error ->
                    setState { state ->
                        state.copy(
                            toastMessage = getDetailsOrMessage(error),
                        )
                    }
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val response = userRepository.followUser(state.value.profile!!.id)
                response.onSuccess {
                    setState { state ->
                        state.copy(
                            profile = state.profile!!.copy(
                                followed = true,
                                follower = state.profile.follower + 1
                            )
                        )
                    }
                }.onFailure { error ->
                    setState { state ->
                        state.copy(
                            toastMessage = getDetailsOrMessage(error),
                        )
                    }
                }
            }
        }
    }
}