package com.example.booksriver.ui.main

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.RemoteList
import com.example.booksriver.data.model.User
import com.example.booksriver.theme.HorizontalPadding
import com.example.booksriver.theme.PaddingMedium
import com.example.booksriver.theme.PaddingSmall
import com.example.booksriver.theme.PaddingXLarge
import com.example.booksriver.util.collectState
import com.example.booksriver.util.getResponsiveColumnCount

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToBookDetail: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {
    val state by viewModel.collectState()

    val isUserLoggedIn = state.isUserLoggedIn
    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn == false) {
            onNavigateToLogin()
        }
    }

    if (state.user == null) {
        BooksriverCircularProgressIndicator(modifier = Modifier.fillMaxHeight())
    } else {
        SwipeRefreshWrapper(state.isRefreshing, viewModel::refresh) {
            MainContent(
                user = state.user!!,
                currentlyReading = state.currentlyReading,
                notStarted = state.notStarted,
                showReadingOrNotStarted = state.showReadingOrNotStarted,
                chipData = state.chipData,
                onChipClicked = viewModel::onChipClicked,
                selectedChipIndex = state.selectedChipIndex,
                onFavouriteClick = viewModel::onFavouriteClick,
                onNavigateToBookDetail = onNavigateToBookDetail,
                onNavigateToBooksScreen = onNavigateToBooksScreen
            )
        }
    }

    if (state.toastMessage != null) {
        Toast.makeText(LocalContext.current, state.toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }
}

@Composable
fun MainContent(
    user: User,
    currentlyReading: ListsData<Book>,
    notStarted: ListsData<Book>,
    showReadingOrNotStarted: ShowReadingOrNotStarted,
    chipData: List<ListsData<Book>>,
    selectedChipIndex: Int,
    onChipClicked: (Int) -> Unit,
    onFavouriteClick: (Boolean, Int) -> Unit,
    onNavigateToBookDetail: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                MainHeader(modifier = Modifier.padding(top = PaddingMedium), user = user)
                CurrentlyReadingOrNotStarted(
                    modifier = Modifier.padding(top = PaddingXLarge),
                    currentlyReading = currentlyReading,
                    notStarted = notStarted,
                    showReadingOrNotStarted = showReadingOrNotStarted,
                    onNavigateToBookDetail = onNavigateToBookDetail
                )
                ChipLists(
                    modifier = Modifier.padding(top = PaddingXLarge),
                    chipData = chipData,
                    selectedChipIndex = selectedChipIndex,
                    onChipClicked = onChipClicked,
                    onFavouriteClick = onFavouriteClick,
                    onNavigateToBookDetail = onNavigateToBookDetail,
                    onNavigateToBooksScreen = onNavigateToBooksScreen
                )
            }
        }
    }
}


@Composable
fun MainHeader(modifier: Modifier = Modifier, user: User) {
    Column(modifier = modifier.padding(horizontal = HorizontalPadding)) {
        BooksriverTitle1("${stringResource(R.string.hi)} ${user.username}")
        BooksriverTitle2(
            stringResource(R.string.what_wanna_read),
            modifier = Modifier.padding(top = PaddingMedium)
        )
    }
}

@Composable
fun CurrentlyReadingOrNotStarted(
    modifier: Modifier = Modifier,
    currentlyReading: ListsData<Book>,
    notStarted: ListsData<Book>,
    showReadingOrNotStarted: ShowReadingOrNotStarted,
    onNavigateToBookDetail: (Int) -> Unit
) {
    if (showReadingOrNotStarted == ShowReadingOrNotStarted.READING) {
        ListsData(
            modifier = modifier,
            listsData = currentlyReading,
            onNavigateToBookDetail = onNavigateToBookDetail
        )
    } else if (showReadingOrNotStarted == ShowReadingOrNotStarted.NOT_STARTED) {
        ListsData(
            modifier = modifier,
            listsData = notStarted,
            onNavigateToBookDetail = onNavigateToBookDetail
        )
    }

}


@Composable
fun ListsData(
    modifier: Modifier = Modifier,
    listsData: ListsData<Book>,
    onNavigateToBookDetail: (Int) -> Unit
) {
    Column(modifier = modifier) {

        if (listsData.data.isLoading || listsData.data.error != null) {
            BooksriverLoadingOrError(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                isCircularLoadingType = false,
                isLoading = listsData.data.isLoading,
                error = listsData.data.error
            )
            return@Column
        }
        if (listsData.data.data.isNotEmpty()) {
            BooksriverTitle1(
                text = if (listsData.titleId == null) listsData.title else stringResource(listsData.titleId),
                modifier = Modifier.padding(horizontal = HorizontalPadding)
            )
            LazyRow(
                modifier = Modifier.padding(top = PaddingSmall),
                contentPadding = PaddingValues(start = HorizontalPadding)
            ) {
                items(listsData.data.data) {
                    HorizontalBookCard(book = it, onNavigateToBookDetail = onNavigateToBookDetail)
                }
            }
        }

    }

}

@Composable
fun ChipLists(
    modifier: Modifier = Modifier,
    chipData: List<ListsData<Book>>,
    selectedChipIndex: Int,
    onChipClicked: (Int) -> Unit,
    onFavouriteClick: (Boolean, Int) -> Unit,
    onNavigateToBookDetail: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = HorizontalPadding)
        ) {
            chipData.forEachIndexed { index, item ->
                TextChip(
                    title = if (item.titleId == null) item.title else stringResource(item.titleId),
                    isSelected = index == selectedChipIndex,
                    onClicked = { onChipClicked(index) },
                    color = item.color,
                    colorDark = item.colorDark
                )
            }
        }
        Column(
            modifier = Modifier.padding(
                horizontal = HorizontalPadding,
                vertical = PaddingSmall
            )
        ) {
            ChipListData(
                chipListData = chipData[selectedChipIndex].data,
                onFavouriteClick = onFavouriteClick,
                onNavigateToBookDetail = onNavigateToBookDetail,
                onNavigateToBooksScreen = onNavigateToBooksScreen
            )
        }
    }
}

@Composable
fun ChipListData(
    chipListData: RemoteList<Book>?,
    onFavouriteClick: (Boolean, Int) -> Unit,
    onNavigateToBookDetail: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {
    if (chipListData != null) {
        if (chipListData.isLoading || chipListData.error != null) {
            BooksriverLoadingOrError(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                isCircularLoadingType = false,
                isLoading = chipListData.isLoading,
                error = chipListData.error
            )
            return
        }

        VerticalResponsiveList(
            item = { book ->
                VerticalBookCard(
                    book = book,
                    onFavouriteClick = onFavouriteClick,
                    onNavigateToBookDetail = onNavigateToBookDetail,
                    onNavigateToBooksScreen = onNavigateToBooksScreen
                )
            }, data = chipListData.data, gridCountLandscape = getResponsiveColumnCount(
                LocalConfiguration.current, 450
            ), isLazy = false
        )


    }
}

@Preview
@Composable
fun MainHeaderPreview() {
    MainHeader(user = User(1, "ciao", "", ""))
}

@Preview
@Composable
fun MainScreenPreview() {
}