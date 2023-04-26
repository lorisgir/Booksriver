package com.example.booksriver.ui.mylibrary

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Book
import com.example.booksriver.theme.HorizontalPadding
import com.example.booksriver.theme.PaddingMedium
import com.example.booksriver.theme.PaddingSmall
import com.example.booksriver.util.collectState
import com.example.booksriver.util.getResponsiveColumnCount
import com.example.booksriver.util.isLandscapeOrTablet
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun MyLibraryScreen(viewModel: MyLibraryViewModel, onBookClick: (Int) -> Unit) {
    val state by viewModel.collectState()

    MyLibraryContent(
        isLoading = state.isLoading,
        error = state.error,
        userBooksGrouped = state.userBooksGrouped,
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::refresh,
        onBookClick = onBookClick
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyLibraryContent(
    isLoading: Boolean,
    error: String?,
    userBooksGrouped: Map<K.BookStatus, List<Book>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onBookClick: (Int) -> Unit
) {
    CollapsingToolbarScaffold(
        modifier = Modifier,
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            TopAppBar(backgroundColor = Color.Transparent, elevation = 0.dp) {
                BooksriverTitle1(
                    text = stringResource(R.string.my_library),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    ) {
        SwipeRefreshWrapper(isRefreshing, onRefresh) {
            Surface(modifier = Modifier.fillMaxSize()) {
                if (isLoading || error != null) {
                    BooksriverLoadingOrError(
                        modifier = Modifier.fillMaxSize(), isLoading = isLoading, error = error
                    )
                    return@Surface
                }
                MyLibraryData(userBooksGrouped, onBookClick)
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyLibraryData(userBooksGrouped: Map<K.BookStatus, List<Book>>, onBookClick: (Int) -> Unit) {
    val configuration = LocalConfiguration.current
    LazyColumn {
        userBooksGrouped.forEach { (status, data) ->
            stickyHeader {
                ListHeader(status)
            }
            gridItems(
                data,
                nColumns = getResponsiveColumnCount(configuration,170)
            ) { item ->
                ListItem(item, onBookClick)
            }
        }
    }
}

@Composable
fun ListHeader(status: K.BookStatus) {
    Box(
        modifier = Modifier
            .padding(horizontal = HorizontalPadding)
            .fillMaxWidth()
            .height(40.dp)
            .background(color = MaterialTheme.colors.surface),
        contentAlignment = Alignment.CenterStart,
    ) {
        TextChip(title = stringResource(status.titleId), color = status.color)
    }
}

@Composable
fun ListItem(book: Book, onBookClick: (Int) -> Unit) {
    Surface(modifier = Modifier.padding(vertical = PaddingSmall)) {
        BookCard(book = book, onNavigateToBookDetail = onBookClick)
    }
}

@Preview
@Composable
fun BookCard(book: Book = Book.build(), onNavigateToBookDetail: (Int) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(end = PaddingMedium)
            .background(color = MaterialTheme.colors.background)
            .border(
                width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp)
            ), elevation = 0.dp
    ) {
        ConstraintLayout(
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        ) {
            val (image, bar) = createRefs()
            BookImage(modifier = Modifier
                .height(170.dp)
                .clickable { onNavigateToBookDetail(book.id) }
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }, imageUrl = book.imageUrl, isBordered = false,
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier
                .height(8.dp)
                .fillMaxWidth(book.userBook.percentage)
                .background(
                    color = Color(book.userBook.status.color),
                )
                .constrainAs(bar) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                })
        }

    }
}


@Preview
@Composable
fun PreviewMyLibraryContent() {
    MyLibraryContent(isLoading = false,
        error = null,
        userBooksGrouped = mapOf(K.BookStatus.NOT_STARTED to listOf(Book.build())),
        isRefreshing = false,
        onRefresh = {},
        onBookClick = {})
}