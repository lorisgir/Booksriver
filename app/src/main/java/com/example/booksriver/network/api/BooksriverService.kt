package com.example.booksriver.network.api

import com.example.booksriver.data.dto.BookDto
import com.example.booksriver.data.dto.CategoryDto
import com.example.booksriver.data.dto.ProfileDto
import com.example.booksriver.data.dto.UserLibraryDto
import com.example.booksriver.data.model.Author
import com.example.booksriver.data.model.Review
import com.example.booksriver.data.model.User
import com.example.booksriver.data.request.*
import retrofit2.Response
import retrofit2.http.*

interface BooksriverService {

    /**
     * USER
     */

    @GET("/user-server/user/search")
    suspend fun searchUsers(@Query("q") q: String): Response<List<User>>

    @GET("/user-server/user/me")
    suspend fun getLoggedUser(): Response<ProfileDto>

    @GET("/user-server/user/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<ProfileDto>

    @GET("/book-server/user-favourite/books")
    suspend fun getBooksByUserFavourites(): Response<List<BookDto>>

    @POST("/user-server/user/me/update")
    suspend fun changeUsername(@Body changeUsernameRequest: ChangeUsernameRequest): Response<User>

    @POST("/user-server/user-follow/{id}/follow")
    suspend fun followUser(@Path("id") id: Int): Response<Unit>

    @POST("/user-server/user-follow/{id}/unfollow")
    suspend fun unfollowUser(@Path("id") id: Int): Response<Unit>

    @GET("/user-server/user-follow/{id}/followers")
    suspend fun getFollower(@Path("id") id: Int): Response<List<User>>

    @GET("/user-server/user-follow/{id}/following")
    suspend fun getFollowing(@Path("id") id: Int): Response<List<User>>

    /**
     * BOOK
     */
    @GET("/book-server/book/{id}")
    suspend fun getBook(@Path("id") id: Int): Response<BookDto>

    @GET("/book-server/book/most-popular")
    suspend fun getMostPopularBooks(): Response<List<BookDto>>

    @GET("/book-server/book/suggested")
    suspend fun getSuggestedBooks(): Response<List<BookDto>>

    @GET("/book-server/category/{id}/books")
    suspend fun getBooksByCategory(@Path("id") id: Int): Response<List<BookDto>>

    @GET("/book-server/book/search")
    suspend fun searchBooks(@Query("q") q: String): Response<List<BookDto>>

    @GET("/book-server/book/{id}/user-libraries")
    suspend fun getUserLibrariesFromBookId(@Path("id") id: Int): Response<List<UserLibraryDto>>

    /**
     * AUTHOR
     */
    @GET("/book-server/author/{id}")
    suspend fun getAuthorById(@Path("id") id: Int): Response<Author>

    @GET("/book-server/author/search")
    suspend fun searchAuthors(@Query("q") q: String): Response<List<Author>>

    @GET("/book-server/author/{id}/books")
    suspend fun getBooksByAuthor(@Path("id") id: Int): Response<List<BookDto>>


    /**
     * CATEGORY
     */
    @GET("/book-server/category/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Response<CategoryDto>

    @GET("/book-server/category/most-popular")
    suspend fun getMostPopularCategories(): Response<List<CategoryDto>>

    @GET("/book-server/category/search")
    suspend fun searchCategories(@Query("q") q: String): Response<List<CategoryDto>>


    /**
     * USER BOOK
     */
    @GET("/book-server/user-book/{status}/books")
    suspend fun getUserBooksByStatus(@Path("status") status: Int): Response<List<BookDto>>

    @GET("/book-server/user-book/books")
    suspend fun getUserBooks(): Response<List<BookDto>>

    @POST("/book-server/user-book/{idBook}/change-status")
    suspend fun changeUserBookStatus(
        @Path("idBook") idBook: Int,
        @Body changeUserBookStatus: ChangeUserBookStatus
    ): Response<Unit>

    @POST("/book-server/user-book/{idBook}/addReading")
    suspend fun addReadingUserBook(@Path("idBook") idBook: Int): Response<Unit>

    @DELETE("/book-server/user-book/{idBook}/remove")
    suspend fun removeBook(@Path("idBook") idBook: Int): Response<Unit>

    @POST("/book-server/user-book/{idBook}/change-pages-read")
    suspend fun changeUserBookPagesRead(
        @Path("idBook") idBook: Int,
        @Body changeUserBookPagesRead: ChangeUserBookPagesRead
    ): Response<Unit>

    @POST("/book-server/user-favourite/{idBook}/add")
    suspend fun addBookToFavourite(@Path("idBook") idBook: Int): Response<Unit>

    @DELETE("/book-server/user-favourite/{idBook}/remove")
    suspend fun removeBookToFavourite(@Path("idBook") idBook: Int): Response<Unit>

    /**
     * USER LIBRARY
     */
    @GET("/book-server/user-library/{id}")
    suspend fun getUserLibraryById(@Path("id") id: Int): Response<UserLibraryDto>

    @GET("/book-server/user-library/search")
    suspend fun searchUserLibraries(@Query("q") q: String): Response<List<UserLibraryDto>>

    @POST("/book-server/user-library/create")
    suspend fun createUserLibrary(@Body createUserLibraryRequest: CreateUserLibraryRequest): Response<UserLibraryDto>

    @POST("/book-server/user-library/{id}/edit")
    suspend fun editUserLibrary(
        @Path("id") id: Int,
        @Body editUserLibraryRequest: EditUserLibraryRequest
    ): Response<UserLibraryDto>

    @DELETE("/book-server/user-library/{id}/delete")
    suspend fun deleteUserLibrary(@Path("id") id: Int): Response<Unit>

    @GET("/book-server/user-library/{id}/books")
    suspend fun getBooksByUserLibrary(@Path("id") id: Int): Response<List<BookDto>>

    @GET("/book-server/user-library/user/{id}/libraries")
    suspend fun getUserLibrariesByUserId(@Path("id") id: Int): Response<List<UserLibraryDto>>

    @POST("/book-server/user-library/adds-book")
    suspend fun addBookToUserLibraries(@Body changeUsernameRequest: AddsBook): Response<Unit>

    @POST("/book-server/user-library/{id}/add-book")
    suspend fun addBookToUserLibrary(@Path("id") id: Int, @Body addBookRequest: AddBookToLibraryRequest): Response<Unit>

    @POST("/book-server/user-library/{id}/remove-book")
    suspend fun removeBookFromUserLibrary(@Path("id") id: Int, @Body removeBookFromLibrary: RemoveBookFromLibraryRequest): Response<Unit>

    /**
     * REVIEW
     */
    @GET("/user-server/review/book/{bookId}/list")
    suspend fun getReviewByBookId(@Path("bookId") id: Int): Response<List<Review>>

    @GET("/user-server/review/book/{bookId}/user/{userId}")
    suspend fun getReviewByUserIdAndBookId(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int,
        ): Response<Review>

    @POST("/user-server/review/create")
    suspend fun createReview(@Body createReview: CreateReview): Response<Review>

    @DELETE("/user-server/review/{id}/delete")
    suspend fun deleteReview(@Path("id") idReview: Int): Response<Unit>

    @POST("/user-server/review/{id}/update")
    suspend fun updateReview(
        @Path("id") idReview: Int,
        @Body updateReview: UpdateReview
    ): Response<Review>
}
