@file:OptIn(FlowPreview::class)

package com.example.booksriver.ui.search


import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.example.booksriver.data.K
import com.example.booksriver.data.model.RemoteList
import com.example.booksriver.repository.*
import com.example.booksriver.util.getDetailsOrMessage
import com.example.booksriver.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val authorRepository: AuthorRepository,
    private val categoryRepository: CategoryRepository,
    private val userLibraryRepository: UserLibraryRepository
) : BaseViewModel<SearchState>(initialState = SearchState()) {

    private val _searchedData = currentState.searchedData.toMutableList()
    private val _text = MutableStateFlow("")

    init {

        _searchedData.add(
            SearchWrapper(
                title = "Categories",
                key = K.SearchType.CATEGORY,
                repoFunction = categoryRepository::searchCategories,
                data = RemoteList(isLoading = false)
            )
        )
        _searchedData.add(
            SearchWrapper(
                title = "Books",
                key = K.SearchType.BOOK,
                searchShowType = K.SearchShowType.GRID,
                repoFunction = bookRepository::searchBooks,
                data = RemoteList(isLoading = false)
            )
        )
        _searchedData.add(
            SearchWrapper(
                title = "Authors",
                key = K.SearchType.AUTHOR,
                repoFunction = authorRepository::searchAuthors,
                data = RemoteList(isLoading = false)
            )
        )
        _searchedData.add(
            SearchWrapper(
                title = "User Libraries",
                key = K.SearchType.USER_LIBRARY,
                searchShowType = K.SearchShowType.GRID,
                repoFunction = userLibraryRepository::searchUserLibraries,
                data = RemoteList(isLoading = false)
            )
        )
        _searchedData.add(
            SearchWrapper(
                title = "Users",
                key = K.SearchType.USER,
                repoFunction = userRepository::searchUsers,
                data = RemoteList(isLoading = false)
            )
        )
        setState { state -> state.copy(searchedData = _searchedData.toMutableStateList()) }

        viewModelScope.launch {
            _text.debounce(500)
                .collect { text ->
                    _searchedData.forEach { item ->
                        search(text, item)
                    }
                }
        }
    }

    fun onSearchTextChanged(searchText: String) {
        _text.value = searchText
        if (searchText.trim().isNotEmpty()) {
            setState { state ->
                _searchedData.forEach { item ->
                    item.data.isLoading = true
                }
                state.copy(
                    searchedData = _searchedData.toMutableStateList(),
                    isSearchTextValid = true,
                    hasSearchedOnce = true,
                    searchText = searchText
                )
            }
        } else {
            _searchedData.forEach { item ->
                item.data.isLoading = false
            }
            setState { state ->
                state.copy(
                    isSearchTextValid = false,
                    searchText = searchText,
                    searchedData = _searchedData.toMutableStateList()
                )
            }
        }

    }


    private fun search(searchText: String, value: SearchWrapper) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = value.repoFunction.invoke(searchText)
            response.onSuccess {
                value.data.isLoading = false
                value.data.data = it.take(10)
                setState { state -> state.copy(searchedData = _searchedData.toMutableStateList()) }
            }.onFailure { error ->
                value.data.isLoading = false
                value.data.error = getDetailsOrMessage(error)
                setState { state -> state.copy(searchedData = _searchedData.toMutableStateList()) }
            }
        }
    }
}
