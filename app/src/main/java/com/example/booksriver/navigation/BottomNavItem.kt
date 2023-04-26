package com.example.booksriver.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.booksriver.R

sealed class BottomNavItem(
    var titleId: Int,
    var iconUnselected: ImageVector,
    var iconSelected: ImageVector,
    var screenRoute: String,
    var screenName: String
) {

    object Main : BottomNavItem(
        R.string.home,
        Icons.Outlined.Home,
        Icons.Filled.Home,
        BooksriverScreens.MainScreen.name,
        BooksriverScreens.MainScreen.name
    )

    object MyLibrary : BottomNavItem(
        R.string.my_library,
        Icons.Outlined.LibraryBooks,
        Icons.Filled.LibraryBooks,
        BooksriverScreens.MyLibraryScreen.name,
        BooksriverScreens.MyLibraryScreen.name
    )

    object Search : BottomNavItem(
        R.string.search,
        Icons.Outlined.Search,
        Icons.Filled.Search,
        BooksriverScreens.SearchScreen.name,
        BooksriverScreens.SearchScreen.name
    )

    object Profile : BottomNavItem(
        R.string.profile,
        Icons.Outlined.AccountCircle,
        Icons.Filled.AccountCircle,
        "${BooksriverScreens.ProfileScreen.name}/0",
        BooksriverScreens.ProfileScreen.name
    )


    companion object {
        fun getItems(): List<BottomNavItem> = listOf(
            Main,
            MyLibrary,
            Search,
            Profile,
        )
    }


}