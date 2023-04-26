package com.example.booksriver.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.K
import com.example.booksriver.data.model.*
import com.example.booksriver.theme.*
import com.example.booksriver.util.collectState
import com.example.booksriver.util.getResponsiveColumnCount


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBookClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {
    val state by viewModel.collectState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize()) {
        BooksriverTextField(
            modifier = Modifier
                .padding(horizontal = HorizontalPadding),
            value = state.searchText,
            label = stringResource(R.string.search_label),
            onValueChange = viewModel::onSearchTextChanged,
            leadingIcon = { Icon(Icons.Outlined.Search, stringResource(R.string.search)) },
            isError = false, //state.isSearchTextValid == false,
            imeAction = ImeAction.Done,
            maxLines = 1,
            onAction = KeyboardActions {
                keyboardController?.hide()
            },
            trailingIcon = {
                if (state.searchText.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchTextChanged("") }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_text_icon_description)
                        )
                    }
                }
            }
        )
        SearchContent(
            isSearchTextValid = state.isSearchTextValid,
            hasSearchedOnce = state.hasSearchedOnce,
            searchedData = state.searchedData,
            onBookClick = onBookClick,
            onUserClick = onUserClick,
            onNavigateToBooksScreen = onNavigateToBooksScreen,
        )
    }


}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchContent(
    isSearchTextValid: Boolean?,
    searchedData: List<SearchWrapper>,
    hasSearchedOnce: Boolean,
    onBookClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit,
) {
    if (isSearchTextValid == false || !hasSearchedOnce) {
        EmptySearchScreen()
        return
    }
    if (isSearchTextValid == true) {
        LazyColumn(
            modifier = Modifier
                .padding(top = PaddingMedium)
                .fillMaxSize()
        ) {
            items(searchedData) { item ->
                Column(modifier = Modifier.padding(bottom = PaddingXLarge)) {
                    BooksriverTitle1(
                        text = item.title,
                        modifier = Modifier.padding(
                            start = HorizontalPadding,
                            bottom = PaddingMedium
                        )
                    )
                    Column {
                        if (item.data.isLoading || item.data.error != null) {
                            BooksriverLoadingOrError(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                isLoading = item.data.isLoading,
                                error = item.data.error,
                                isCircularLoadingType = false
                            )
                        } else {
                            when (item.searchShowType) {
                                K.SearchShowType.ROW ->
                                    RowSearchResult(
                                        item = item,
                                        onUserClick = onUserClick,
                                        onBookClick = onBookClick,
                                        onNavigateToBooksScreen = onNavigateToBooksScreen
                                    )
                                K.SearchShowType.GRID -> GridSearchResult(
                                    item = item,
                                    onUserClick = onUserClick,
                                    onBookClick = onBookClick,
                                    onNavigateToBooksScreen = onNavigateToBooksScreen
                                )
                                else -> Box {}
                            }
                        }
                    }
                }

            }
        }
    }
}


@Composable
@Preview
fun BookCard(item: Book = Book.build(), onClick: (Int) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(bottom = PaddingLarge, start = HorizontalPadding, end = HorizontalPadding)
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(item.id) },
        backgroundColor = MaterialTheme.colors.cardBackgroundColor
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            BookImage(
                modifier = Modifier.size(64.dp),
                imageUrl = item.imageUrl,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(PaddingMedium)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                BooksriverTitle1(text = item.title, fontSize = 15.sp)
                if (item.authors.isNotEmpty()) {
                    BooksriverTitle2(text = item.authors.first().name, fontSize = 13.sp)
                }
            }
        }
    }
}


@Composable
fun UserLibraryCard(item: UserLibrary, onClick: (String, Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(bottom = PaddingLarge, start = HorizontalPadding, end = HorizontalPadding)
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(K.ListBookType.USER_LIBRARY.type, item.id) },
        backgroundColor = MaterialTheme.colors.cardBackgroundColor
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.bookshelf),
                contentDescription = stringResource(R.string.bookshelf_icon_description),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = PaddingMedium)
                    .width(64.dp)
                    .height(51.dp)
            )
            Column(
                modifier = Modifier
                    .padding(PaddingMedium)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                BooksriverTitle1(text = item.name, fontSize = 15.sp)
                BooksriverTitle2(
                    text = "${item.count} ${stringResource(R.string.books)}",
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun AuthorCard(item: Author, onClick: (String, Int) -> Unit) {
    PersonCard(
        text = item.name,
        imageResource = R.drawable.author_pic,
        onClick = { onClick(K.ListBookType.AUTHOR.type, item.id) }
    )
}


@Composable
fun CategoryCard(item: Category, onClick: (String, Int) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(end = PaddingLarge)
            .height(64.dp)
            .width(96.dp)
    ) {
        TextChip(
            title = item.name,
            color = item.color,
            colorDark = item.colorDark,
            onClicked = { onClick(K.ListBookType.CATEGORY.type, item.id) })
    }
}

@Composable
fun UserCard(item: User, onClick: (Int) -> Unit) {
    PersonCard(
        text = item.username,
        imageResource = R.drawable.profile_pic,
        onClick = { onClick(item.id) }
    )
}

@Composable
fun PersonCard(text: String, imageResource: Int, onClick: () -> Unit) {
    Surface(modifier = Modifier
        .padding(end = PaddingLarge)
        .clip(shape = RoundedCornerShape(8.dp))
        .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(imageResource),
                contentDescription = stringResource(R.string.profile_pic_icon_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            BooksriverTitle2(
                text = text,
                modifier = Modifier.width(64.dp),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
        }
    }
}


@Composable
fun RowSearchResult(
    item: SearchWrapper,
    onBookClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {
    if (item.data.data.isEmpty()) {
        BooksriverTitle2(
            stringResource(R.string.no_data_found),
            modifier = Modifier.padding(start = HorizontalPadding)
        )
    } else {

        val cardItem: @Composable (ISearch, K.SearchType) -> Unit = { item, key ->
            when (key) {
                K.SearchType.CATEGORY ->
                    CategoryCard(item = item as Category, onClick = onNavigateToBooksScreen)
                K.SearchType.USER ->
                    UserCard(item = item as User, onClick = onUserClick)
                K.SearchType.AUTHOR ->
                    AuthorCard(item = item as Author, onClick = onNavigateToBooksScreen)
                K.SearchType.BOOK ->
                    BookCard(item = item as Book, onClick = onBookClick)
                K.SearchType.USER_LIBRARY ->
                    UserLibraryCard(item = item as UserLibrary, onClick = onNavigateToBooksScreen)
            }
        }

        LazyRow(
            modifier = Modifier.padding(top = PaddingSmall),
            contentPadding = PaddingValues(start = HorizontalPadding)
        ) {
            items(item.data.data) {
                cardItem(it, item.key)
            }
        }
    }

}


@Composable
fun GridSearchResult(
    item: SearchWrapper,
    onBookClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {
    if (item.data.data.isEmpty()) {
        BooksriverTitle2(
            stringResource(R.string.no_data_found),
            modifier = Modifier.padding(start = HorizontalPadding)
        )
    } else {
        Row(modifier = Modifier.fillMaxWidth()) {

            val cardItem: @Composable (ISearch, K.SearchType) -> Unit = { item, key ->
                when (key) {
                    K.SearchType.BOOK ->
                        BookCard(item = item as Book, onClick = onBookClick)
                    K.SearchType.USER_LIBRARY ->
                        UserLibraryCard(
                            item = item as UserLibrary,
                            onClick = onNavigateToBooksScreen
                        )
                    K.SearchType.AUTHOR ->
                        AuthorCard(item = item as Author, onClick = onNavigateToBooksScreen)
                    K.SearchType.USER ->
                        UserCard(item = item as User, onClick = onUserClick)
                    K.SearchType.CATEGORY ->
                        CategoryCard(item = item as Category, onClick = onNavigateToBooksScreen)
                }
            }

            VerticalResponsiveList(
                item = { cardItem(it, item.key) },
                data = item.data.data,
                gridCountPortrait = 2,
                gridCountLandscape = getResponsiveColumnCount(LocalConfiguration.current, 350),
                isLazy = false
            )
        }
    }

}


@Composable
fun SearchScreenPreview() {
}