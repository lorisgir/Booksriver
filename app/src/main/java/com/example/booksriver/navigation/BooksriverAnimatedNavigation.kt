package com.example.booksriver.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.booksriver.ui.book.BookScreen
import com.example.booksriver.ui.books.BooksScreen
import com.example.booksriver.ui.follow.FollowScreen
import com.example.booksriver.ui.login.LoginScreen
import com.example.booksriver.ui.main.MainScreen
import com.example.booksriver.ui.mylibrary.MyLibraryScreen
import com.example.booksriver.ui.profile.ProfileScreen
import com.example.booksriver.ui.review.ReviewScreen
import com.example.booksriver.ui.search.SearchScreen
import com.example.booksriver.ui.manage_library.ManageLibraryScreen
import com.example.booksriver.ui.signup.SignUpScreen
import com.example.booksriver.util.isLandscape
import com.example.booksriver.util.isTablet
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BooksriverAnimatedNavigation(
    navController: NavHostController,
    startScreen: String = BooksriverScreens.MainScreen.name,
    paddingValues: PaddingValues
) {
    AnimatedNavHost(
        navController,
        startDestination = startScreen,
        modifier = Modifier.animateContentSize()
    ) {
        composable(BooksriverScreens.SignupScreen.name,
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            SignUpScreen(
                viewModel = hiltViewModel(),
                onNavigateToLogin = { navController.popAllAndNavigateTo(BooksriverScreens.LoginScreen.name) },
                onNavigateToMain = { navController.popAllAndNavigateTo(BooksriverScreens.MainScreen.name) }
            )
        }
        composable(BooksriverScreens.LoginScreen.name,
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            LoginScreen(
                viewModel = hiltViewModel(),
                onNavigateToSignUp = { navController.popAllAndNavigateTo(BooksriverScreens.SignupScreen.name) },
                onNavigateToMain = { navController.popAllAndNavigateTo(BooksriverScreens.MainScreen.name) }
            )
        }
        composable(
            BooksriverScreens.MainScreen.name,
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            },
        ) {
            Box(modifier = Modifier.padding(paddingValues)) {
                MainScreen(
                    viewModel = hiltViewModel(),
                    onNavigateToLogin = { navController.popAllAndNavigateTo(BooksriverScreens.LoginScreen.name) },
                    onNavigateToBookDetail = { bookId -> navController.navigateToBookDetail(bookId) },
                    onNavigateToBooksScreen = { listType, listId ->
                        navController.navigateToBooksScreen(listType, listId)
                    }
                )
            }
        }
        composable(BooksriverScreens.SearchScreen.name,
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            Box(modifier = Modifier.padding(paddingValues)) {
                SearchScreen(
                    viewModel = hiltViewModel(),
                    onBookClick = { bookId -> navController.navigateToBookDetail(bookId) },
                    onUserClick = { userId -> navController.navigateToVisitProfileScreen(userId) },
                    onNavigateToBooksScreen = { listType, listId ->
                        navController.navigateToBooksScreen(listType, listId)
                    }
                )
            }
        }
        composable(BooksriverScreens.MyLibraryScreen.name,
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            Box(modifier = Modifier.padding(paddingValues)) {
                MyLibraryScreen(
                    viewModel = hiltViewModel(),
                    onBookClick = { bookId -> navController.navigateToBookDetail(bookId) },
                )
            }
        }
        composable("${BooksriverScreens.ProfileScreen.name}/{userId}",
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            Box(modifier = Modifier.padding(paddingValues)) {
                ProfileScreen(
                    viewModel = hiltViewModel(),
                    onNavigateBack = navController.navigateBack(),
                    onNavigateToLogin = {
                        navController.popAllAndNavigateTo(BooksriverScreens.LoginScreen.name)
                    },
                    onNavigateToBooksScreen = { listType, listId ->
                        navController.navigateToBooksScreen(listType, listId)
                    },
                    onNavigateToFollowScreen = { userId, type ->
                        navController.navigateToFollowScreen(userId, type)
                    }
                )
            }
        }
        composable("${BooksriverScreens.VisitProfileScreen.name}/{userId}",
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            Box(modifier = Modifier.padding(paddingValues)) {
                ProfileScreen(
                    viewModel = hiltViewModel(),
                    onNavigateBack = navController.navigateBack(),
                    onNavigateToLogin = {
                        navController.popAllAndNavigateTo(BooksriverScreens.LoginScreen.name)
                    },
                    onNavigateToBooksScreen = { listType, listId ->
                        navController.navigateToBooksScreen(listType, listId)
                    },
                    onNavigateToFollowScreen = { userId, type ->
                        navController.navigateToFollowScreen(userId, type)
                    }
                )
            }
        }
        composable(
            "${BooksriverScreens.BookScreen.name}/{bookId}",
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            },
        ) {
            if (!isTablet(LocalConfiguration.current) || !isLandscape(LocalConfiguration.current)) {
                BookScreen(
                    viewModel = hiltViewModel(),
                    onNavigateBack = navController.navigateBack(),
                    onNavigateToBooksScreen = { listType, listId ->
                        navController.navigateToBooksScreen(listType, listId)
                    },
                    onNavigateToReviewScreen = { bookId ->
                        navController.navigateToReviewScreen(bookId)
                    },
                )
            } else {
                Row {
                    Surface(modifier = Modifier.weight(0.5f)) {
                        BookScreen(
                            viewModel = hiltViewModel(),
                            splitted = true,
                            onNavigateBack = navController.navigateBack(),
                            onNavigateToBooksScreen = { listType, listId ->
                                navController.navigateToBooksScreen(listType, listId)
                            },
                            onNavigateToReviewScreen = { bookId ->
                                navController.navigateToReviewScreen(bookId)
                            }
                        )
                    }
                    Surface(modifier = Modifier.weight(0.5f)) {
                        ReviewScreen(
                            viewModel = hiltViewModel(),
                            isLandscape = true,
                            onNavigateBack = navController.navigateBack(),
                        )
                    }
                }
            }
        }
        composable("${BooksriverScreens.ReviewScreen.name}/{bookId}",
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            ReviewScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = navController.navigateBack(),
            )
        }
        composable("${BooksriverScreens.BooksScreen.name}/{listType}/{listId}",
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            BooksScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = navController.navigateBack(),
                onBookClick = { bookId -> navController.navigateToBookDetail(bookId) },
                onNavigateToBooksScreen = { listType, listId ->
                    navController.navigateToBooksScreen(listType, listId)
                },
                onNavigateToManageLibraryScreen = { libId ->
                    navController.navigateToManageLibraryScreen(libId)
                }
            )
        }
        composable("${BooksriverScreens.FollowScreen.name}/{userId}/{followType}",
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            FollowScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = navController.navigateBack(),
                onNavigateToProfileScreen = { userId ->
                    navController.navigateToVisitProfileScreen(
                        userId
                    )
                },
            )
        }
        composable("${BooksriverScreens.ManageLibraryScreen.name}/{libId}",
            enterTransition = {
                fadeIn(initialAlpha = 0.0f, animationSpec = tween(750))
            },
            exitTransition = {
                fadeOut(targetAlpha = 0.0f, animationSpec = tween(750))
            }) {
            ManageLibraryScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = navController.navigateBack()
            )
        }
    }
}

fun NavController.popAllAndNavigateTo(screenName: String) = navigate(screenName) {
    popUpTo(currentBackStackEntry?.destination?.route ?: return@navigate) {
        inclusive = true
    }
}

fun NavController.navigateBack(): () -> Unit = { popBackStack() }
fun NavController.navigateToBookDetail(bookId: Int) =
    navigate("${BooksriverScreens.BookScreen.name}/$bookId")

fun NavController.navigateToBooksScreen(listType: String, listId: Int) =
    navigate("${BooksriverScreens.BooksScreen.name}/$listType/$listId")

fun NavController.navigateToReviewScreen(bookId: Int) =
    navigate("${BooksriverScreens.ReviewScreen.name}/$bookId")

fun NavController.navigateToProfileScreen(userId: Int) =
    navigate("${BooksriverScreens.ProfileScreen.name}/$userId")

fun NavController.navigateToVisitProfileScreen(userId: Int) =
    navigate("${BooksriverScreens.VisitProfileScreen.name}/$userId")

fun NavController.navigateToFollowScreen(userId: Int, followType: String) =
    navigate("${BooksriverScreens.FollowScreen.name}/$userId/$followType")

fun NavController.navigateToManageLibraryScreen(libId: Int) =
    navigate("${BooksriverScreens.ManageLibraryScreen.name}/$libId")