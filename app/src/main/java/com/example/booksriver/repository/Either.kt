package com.example.booksriver.repository

/**
 * Result wrapper class which can have only two possible states i.e. Either success or failure
 */
sealed class Either<T, E> {
    data class Success<T>(val data: T) : Either<T, Nothing>()
    data class Error<E>(val error: E) : Either<Nothing, E>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <E> error(error: E) = Error(error)
    }

    inline fun onSuccess(block: (T) -> Unit): Either<T, E> = apply {
        if (this is Success) {
            block(data)
        }
    }

    inline fun onFailure(block: (E) -> Unit): Either<T, E> = apply {
        if (this is Error) {
            block(error)
        }
    }
}
