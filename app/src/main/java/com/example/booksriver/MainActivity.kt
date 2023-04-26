package com.example.booksriver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.booksriver.component.ConnectivityStatus
import com.example.booksriver.component.ConnectivityStatusBox
import com.example.booksriver.data.K
import com.example.booksriver.data.Singleton
import com.example.booksriver.navigation.*
import com.example.booksriver.network.ConnectionState
import com.example.booksriver.network.connectivityState
import com.example.booksriver.theme.BooksriverTheme
import com.example.booksriver.util.isLandscapeOrTablet
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()

        val b = intent.extras
        var isUserLoggedIn = false
        var bookId: Int? = null
        if (b != null) {
            isUserLoggedIn = b.getBoolean("isUserLoggedIn", false)
            if (b.containsKey("bookId")) {
                bookId = b.getInt("bookId", 0)
            }
        }


        val startScreen =
            if (isUserLoggedIn) BooksriverScreens.MainScreen.name else BooksriverScreens.LoginScreen.name
        val navigateTo =
            if (isUserLoggedIn && bookId != null) "${BooksriverScreens.BookScreen.name}/{bookId}" else null

        setContent {
            BooksRiverApp(savedInstanceState == null, startScreen, navigateTo, bookId)
        }
    }

    override fun onResume() {
        super.onResume()
        setTheme()
    }

    private fun setTheme() {
        Singleton.isDarkMode =
            (applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun BooksRiverApp(init: Boolean, startScreen: String, navigateTo: String?, param: Int?) {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (navigateTo == "${BooksriverScreens.BookScreen.name}/{bookId}" && param != null && init) {
        LaunchedEffect(Unit) {
            navController.navigateToBookDetail(param)
        }
    }

    val showNavigation = remember(currentRoute) {
        when (currentRoute?.split("/")?.get(0)) {
            BooksriverScreens.MainScreen.name,
            BooksriverScreens.SearchScreen.name,
            BooksriverScreens.ProfileScreen.name,
            BooksriverScreens.MyLibraryScreen.name -> true
            else -> false
        }
    }

    val onBottomNavigationClick = fun(item: BottomNavItem) {
        navController.navigate(item.screenRoute) {
            navController.graph.startDestinationRoute?.let { screen_route ->
                popUpTo(screen_route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val isRail = isLandscapeOrTablet(LocalConfiguration.current)

    BooksriverTheme {
        Scaffold(bottomBar = {
            AnimatedVisibility(
                visible = showNavigation && !isRail,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500)),
                content = {
                    BooksriverBottomNavigation(onBottomNavigationClick, currentRoute)
                }
            )
        }
        ) { paddingValues ->
            Row {
                AnimatedVisibility(
                    visible = showNavigation && isRail,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(500)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(500)
                    ),
                    content = {
                        BooksriverRailNavigation(onBottomNavigationClick, currentRoute)
                    }
                )
                Column() {
                    val connection by connectivityState()
                    val isConnected = connection === ConnectionState.Available
                    ConnectivityStatus(isConnected)
                    BooksriverAnimatedNavigation(navController, startScreen, paddingValues)
                }
            }
        }
    }
}

