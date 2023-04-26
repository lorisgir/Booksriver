@file:Suppress("UNCHECKED_CAST")

package com.example.booksriver.repository


import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.Profile
import com.example.booksriver.data.model.User
import com.example.booksriver.data.request.ChangeUsernameRequest
import com.example.booksriver.data.request.LoginRequest
import com.example.booksriver.data.request.LoginSocialRequest
import com.example.booksriver.data.request.RegisterRequest
import com.example.booksriver.data.response.ErrorResponse
import com.example.booksriver.network.api.AuthService
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.util.getDefault
import com.example.booksriver.util.getDefaultFromDto
import com.example.booksriver.util.getDefaultListFromDto
import com.example.booksriver.util.getErrorResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject internal constructor(
    private val booksriverService: BooksriverService
) {

    suspend fun searchUsers(q: String): Either<out List<User>, out ErrorResponse> {
        return runCatching {
            booksriverService.searchUsers(q).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun getBooksByUserFavourites(): Either<out List<Book>, out ErrorResponse> {
        return runCatching {
            booksriverService.getBooksByUserFavourites().getDefaultListFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out List<Book>, out ErrorResponse>
    }

    suspend fun getLoggedUser(): Either<out Profile, out ErrorResponse> {
        return runCatching {
            booksriverService.getLoggedUser().getDefaultFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out Profile, out ErrorResponse>
    }

    suspend fun getUser(id: Int): Either<out Profile, out ErrorResponse> {
        return runCatching {
            booksriverService.getUser(id).getDefaultFromDto()
        }.getOrDefault(Either.error(ErrorResponse())) as Either<out Profile, out ErrorResponse>
    }

    suspend fun changeUsername(name: String): Either<out User, out ErrorResponse> {
        return runCatching {
            booksriverService.changeUsername(ChangeUsernameRequest(name)).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun followUser(id: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.followUser(id).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun unfollowUser(id: Int): Either<out Unit, out ErrorResponse> {
        return runCatching {
            booksriverService.unfollowUser(id).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun getFollower(id: Int): Either<out List<User>, out ErrorResponse> {
        return runCatching {
            booksriverService.getFollower(id).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }

    suspend fun getFollowing(id: Int): Either<out List<User>, out ErrorResponse> {
        return runCatching {
            booksriverService.getFollowing(id).getDefault()
        }.getOrDefault(Either.error(ErrorResponse()))
    }
}
