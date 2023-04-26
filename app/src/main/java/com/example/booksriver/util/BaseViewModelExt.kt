package com.example.booksriver.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.booksriver.view.BaseViewModel
import com.example.booksriver.view.state.IState

/**
 * Collects values from this ViewModel's state and represents its latest value via State.
 * Every time there would be new value posted into the state the returned State will be
 * updated causing recomposition of every State.value usage.
 */
@Composable
fun <S : IState, VM : BaseViewModel<S>> VM.collectState() = state.collectAsState()
