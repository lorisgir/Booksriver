package com.example.booksriver.ui.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.example.booksriver.data.K
import com.example.booksriver.data.model.*
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.repository.Either
import com.example.booksriver.view.state.IState
import okhttp3.internal.immutableListOf
import kotlin.reflect.KSuspendFunction1

data class SearchState(
    val searchText: String = "",
    val isSearchTextValid: Boolean? = null,
    val hasSearchedOnce: Boolean = false,
    val isLoading: Boolean? = null,
    val searchedData: List<SearchWrapper> = mutableStateListOf(),
) : IState

data class SearchWrapper(
    val title: String,
    val key: K.SearchType,
    val searchShowType: K.SearchShowType = K.SearchShowType.ROW,
    val repoFunction: KSuspendFunction1<String, Either<out List<ISearch>, out ErrorResponse>>,
    val data: RemoteList<ISearch> = RemoteList(isLoading = false)
)

