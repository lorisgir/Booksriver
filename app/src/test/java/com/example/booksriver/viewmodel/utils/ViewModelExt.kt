package com.example.booksriver.viewmodel.utils

import com.example.booksriver.view.BaseViewModel
import com.example.booksriver.view.state.IState
import io.kotest.matchers.shouldBe

/**
 * Kotest matcher for verifying and comparing the ViewModel's current state
 */
infix fun <S : IState, VM : BaseViewModel<S>> VM.currentStateShouldBe(expected: S) {
    currentState shouldBe expected
}

/**
 * Utility on ViewModel for getting current state in the [block] lambda.
 * Useful in verifying each contents of the state
 */
infix fun <S : IState, VM : BaseViewModel<S>> VM.withState(block: S.() -> Unit) =
    currentState.run(block)
