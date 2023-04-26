package com.example.booksriver.ui.follow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.repository.UserRepository
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<FollowState>(initialState = FollowState()) {

    private val _users = currentState.users.toMutableList()

    init {
        val userId = savedStateHandle.get<String>(K.PARAM_USER_ID)
        val followTypeId = savedStateHandle.get<String>(K.PARAM_FOLLOW_TYPE)
        if (userId != null && followTypeId != null) {
            val followType = K.ListFollowType.values().first { it.type == followTypeId }
            setState { state -> state.copy(followType = followType) }
            getUsers(userId.toInt(), followType)
        }
    }

    private fun getUsers(userId: Int, followType: K.ListFollowType) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                if (followType == K.ListFollowType.FOLLOWERS) userRepository.getFollower(userId) else userRepository.getFollowing(
                    userId
                )
            response.onSuccess { data ->
                _users.addAll(data)
                setState { state ->
                    state.copy(
                        isLoading = false,
                        users = _users
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
}