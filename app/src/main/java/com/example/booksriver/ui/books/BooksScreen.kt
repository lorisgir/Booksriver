package com.example.booksriver.ui.books

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.BooksriverDropdownMenuItem
import com.example.booksriver.data.model.Book
import com.example.booksriver.theme.HorizontalPadding
import com.example.booksriver.theme.iconColor
import com.example.booksriver.ui.components.userlibrary_dialog.UserLibraryDialog
import com.example.booksriver.util.collectState
import com.example.booksriver.util.getResponsiveColumnCount

@Composable
fun BooksScreen(
    viewModel: BooksViewModel,
    onNavigateBack: () -> Unit,
    onBookClick: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    onNavigateToManageLibraryScreen: (Int) -> Unit,
) {
    val state by viewModel.collectState()

    LaunchedEffect(viewModel) {
        viewModel.refresh()
    }

    BooksContent(
        state = state,
        viewModel = viewModel,
        onNavigateBack = onNavigateBack,
        onBookClick = onBookClick,
        onNavigateToBooksScreen = onNavigateToBooksScreen,
        onNavigateToManageLibraryScreen = onNavigateToManageLibraryScreen
    )

    if (state.toastMessage != null) {
        Toast.makeText(LocalContext.current, state.toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }

    if (state.isLibraryDeleted) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
    }

    if (state.isEditLibraryDialogVisible) {
        UserLibraryDialog(
            viewModel = hiltViewModel(),
            onDismissed = viewModel::toggleEditLibraryNameDialog,
            onConfirmed = viewModel::onUserLibraryNameEdited,
            onConfirmedError = viewModel::onUserLibraryNameEditedError,
            isCreate = false,
            libraryId = state.listId,
            libraryName = state.listTitle
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BooksContent(
    state: BooksState,
    viewModel: BooksViewModel,
    onNavigateBack: () -> Unit,
    onBookClick: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    onNavigateToManageLibraryScreen: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                BooksriverTitle1(
                    text = state.listTitle ?: stringResource(R.string.none),
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis
                )
            }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.TwoTone.ArrowBack,
                        contentDescription = stringResource(R.string.go_back),
                        tint = MaterialTheme.colors.iconColor
                    )
                }
            }, actions = {
                if (state.isPersonalUserLibrary == true) {
                    Row {
                        IconButton(onClick = {
                            onNavigateToManageLibraryScreen(state.userLibraryId!!)
                        }) {
                            Icon(
                                imageVector = Icons.Default.LibraryAdd,
                                contentDescription = stringResource(R.string.add_to_library),
                                tint = MaterialTheme.colors.iconColor
                            )
                        }

                        BooksriverDropdownMenu(
                            items = listOf(
                                BooksriverDropdownMenuItem(
                                    text = stringResource(R.string.edit_name),
                                    callback = viewModel::toggleEditLibraryNameDialog
                                ),
                                BooksriverDropdownMenuItem(
                                    text = stringResource(R.string.delete),
                                    callback = viewModel::deleteUserLibrary
                                )
                            )
                        )

                    }
                }
            }, backgroundColor = Color.Transparent, elevation = 0.dp
            )
        },
    ) {
        Surface(modifier = Modifier.padding(horizontal = HorizontalPadding)) {
            if (state.isLoading || state.error != null) {
                BooksriverLoadingOrError(
                    modifier = Modifier.fillMaxSize(),
                    isLoading = state.isLoading,
                    error = state.error
                )
                return@Surface
            }
            BooksData(
                state.books,
                viewModel::onFavouriteClick,
                onBookClick,
                onNavigateToBooksScreen
            )
        }
    }
}


@Composable
fun BooksData(
    books: List<Book>,
    onFavouriteClick: (Boolean, Int) -> Unit,
    onBookClick: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {
    VerticalResponsiveList(
        item = { book ->
            VerticalBookCard(
                book = book,
                onFavouriteClick = onFavouriteClick,
                onNavigateToBookDetail = onBookClick,
                onNavigateToBooksScreen = onNavigateToBooksScreen
            )
        },
        data = books,
        gridCountLandscape = getResponsiveColumnCount(LocalConfiguration.current, 450),
        isLazy = true,
        messageIfEmpty = true
    )
}

