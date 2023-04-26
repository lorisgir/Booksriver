package com.example.booksriver.viewmodel.base

import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Base spec for testing ViewModel.
 *
 * Since we are using `viewModelScope` in the ViewModel which uses Main dispatcher, this spec
 * sets Test dispatcher as a Main dispatcher so that it becomes easy to test the ViewModel.
 */
abstract class ViewModelBehaviorSpec(body: BehaviorSpec.() -> Unit = {}) : BehaviorSpec({
    val dispatcher = TestCoroutineDispatcher()

    coroutineTestScope = true
    Dispatchers.setMain(dispatcher)

    apply(body)

    afterSpec { Dispatchers.resetMain() }
})
