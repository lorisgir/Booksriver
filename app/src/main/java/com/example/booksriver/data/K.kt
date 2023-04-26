package com.example.booksriver.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.booksriver.BuildConfig
import com.example.booksriver.R
import com.example.booksriver.theme.AllColors

object K {
    const val API_BASE_URL = BuildConfig.API_BASE_URL;

    const val SOCIAL_LOGIN_REQUEST_CODE = 1234

    const val CHANNEL_ID = "Booksriver"

    const val PARAM_BOOK_ID = "bookId"
    const val PARAM_LIST_ID = "listId"
    const val PARAM_LIST_TYPE = "listType"
    const val PARAM_USER_ID = "userId"
    const val PARAM_FOLLOW_TYPE = "followType"
    const val PARAM_LIB_ID = "libId"

    const val MAIN_CATEGORIES_NO = 2

    enum class BookStatus(
        val status: Int,
        val titleId: Int,
        val buttonTextId: Int,
        val color: Int
    ) {
        NOT_STARTED(0, R.string.not_started_title, R.string.not_started_btn_text, AllColors.ORANGE[5]),
        READING(1, R.string.currently_reading_title, R.string.currently_reading_btn_text, AllColors.BLUE[5]),
        FINISHED(2, R.string.finished_title, R.string.finished_btn_text, AllColors.GREEN[5]),
        DROPPED(3, R.string.dropped_title, R.string.dropped_btn_text, AllColors.RED[5]),
    }

    enum class SearchType(val key: String) {
        USER("user"),
        CATEGORY("category"),
        AUTHOR("author"),
        BOOK("book"),
        USER_LIBRARY("user_library"),
    }

    enum class SearchShowType(val type: String) {
        ROW("row"),
        GRID("grid"),
        COLUMN("column")
    }

    enum class ListBookType(val type: String) {
        CATEGORY("category"),
        AUTHOR("author"),
        FAVOURITE("favourite"),
        USER_LIBRARY("user_library")
    }

    enum class ListFollowType(val type: String) {
        FOLLOWERS("followers"),
        FOLLOWING("following"),
    }

    data class TextIcon(val textId: Int, val icon: ImageVector)

    val splashHint = listOf(
        TextIcon(R.string.splash_hint_1, Icons.Default.LocalLibrary),
        TextIcon(R.string.splash_hint_2, Icons.Default.Explore),
        TextIcon(R.string.splash_hint_3, Icons.Default.Analytics),
        TextIcon(R.string.splash_hint_4, Icons.Default.ThumbUp)
    )

}