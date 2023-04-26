package com.example.booksriver.util

import com.example.booksriver.data.model.IDto
import com.example.booksriver.data.model.IModel
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.repository.Either
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

inline fun <reified T> Response<T>.getErrorResponse(): ErrorResponse {
    errorBody()
    return Gson().fromJson(errorBody()!!.charStream(), object : TypeToken<ErrorResponse>() {}.type)
}

inline fun <reified T> Response<T>.getDefault(): Either<out T, out ErrorResponse> {
    return if (isSuccessful) {
        Either.success(body()!!)
    } else {
        Either.error(getErrorResponse())
    }
}

inline fun <reified T> Response<T>.getEmpty(): Either<out T?, out ErrorResponse> {
    return if (isSuccessful) {
        Either.success(null)
    } else {
        Either.error(getErrorResponse())
    }
}

inline fun <reified T> Response<List<T>>.getDefaultListFromDto(): Either<out List<IModel>, out ErrorResponse> where T : IDto {
    return try {
        if (isSuccessful) {
            Either.success(body()!!.map { it.fromDto() })
        } else {
            Either.error(getErrorResponse())
        }
    } catch (e: Exception) {
        Either.error(ErrorResponse(message = e.message.toString()))
    }

}

inline fun <reified T> Response<T>.getDefaultFromDto(): Either<out IModel, out ErrorResponse> where T : IDto {
    return try {
        return if (isSuccessful) {
            Either.success(body()!!.fromDto())
        } else {
            Either.error(getErrorResponse())
        }
    } catch (e: Exception) {
        Either.error(ErrorResponse(message = e.message.toString()))
    }
}


inline fun <reified T> Response<T>.getResponse(): T {
    val responseBody = body()
    return if (this.isSuccessful && responseBody != null) {
        responseBody
    } else {
        Gson().fromJson(errorBody()!!.charStream(), object : TypeToken<ErrorResponse>() {}.type)
    }
}