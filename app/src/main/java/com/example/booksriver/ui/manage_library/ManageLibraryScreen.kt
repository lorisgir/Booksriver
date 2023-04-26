@file:OptIn(
    ExperimentalPagerApi::class, ExperimentalPagerApi::class,
    ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class
)

package com.example.booksriver.ui.manage_library

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.model.Book
import com.example.booksriver.theme.*
import com.example.booksriver.util.collectState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ManageLibraryScreen(
    viewModel: ManageLibraryViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.collectState()

    if (state.toastMessage != null) {
        Toast.makeText(LocalContext.current, state.toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                BooksriverTitle1(
                    text = stringResource(R.string.manage_library),
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
                TopAppBarIconPlaceholder()
            }, backgroundColor = Color.Transparent, elevation = 0.dp)
        },
    ) {
        Surface {
            Column {
                TabWrapper(tabs = state.tabs) { page ->
                    if (page == 0) {
                        TabAdd(
                            searchText = state.searchText,
                            isLoading = state.isLoadingTabAdd,
                            error = state.errorTabAdd,
                            searchedData = state.searchedData,
                            hasSearchedOnce = state.hasSearchedOnce,
                            isSearchTextValid = state.isSearchTextValid,
                            onSearchTextChanged = viewModel::onSearchTextChanged,
                            addBookToLib = viewModel::addBookToLib
                        )
                    } else {
                        TabDelete(
                            isLoading = state.isLoadingTabAdd,
                            error = state.errorTabAdd,
                            searchedData = state.libraryBooks,
                            removeBookFromLib = viewModel::removeBookFromLib
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TabWrapper(tabs: List<Int>, content: @Composable (Int) -> Unit) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) {
        tabs.forEachIndexed { index, stringId ->
            Tab(
                text = {
                    BooksriverTitle2(
                        stringResource(stringId),
                        color = if (index == pagerState.currentPage) MaterialTheme.colors.textPrimaryColor else MaterialTheme.colors.textSecondaryColor
                    )
                },
                selected = index == pagerState.currentPage,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }

    }
    HorizontalPager(
        count = tabs.size,
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        Card(
            modifier = Modifier
                .padding(PaddingMedium)
                .fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp
        ) {
            content(page)
        }
    }
}

@Composable
fun TabAdd(
    searchText: String,
    isLoading: Boolean,
    error: String?,
    searchedData: List<AnimatedItem<Book>>,
    isSearchTextValid: Boolean?,
    hasSearchedOnce: Boolean,
    onSearchTextChanged: (String) -> Unit,
    addBookToLib: (AnimatedItem<Book>) -> Unit
) {
    Column {
        SearchField(searchText, onSearchTextChanged)

        if (isSearchTextValid == false || !hasSearchedOnce) {
            EmptySearchScreen()
        } else {
            ListContent(
                isLoading = isLoading,
                error = error,
                searchedData = searchedData,
                onBookAdded = addBookToLib
            )
        }
    }
}

@Composable
fun TabDelete(
    isLoading: Boolean,
    error: String?,
    searchedData: List<AnimatedItem<Book>>,
    removeBookFromLib: (AnimatedItem<Book>) -> Unit
) {
    ListContent(
        isLoading = isLoading,
        error = error,
        searchedData = searchedData,
        onBookRemoved = removeBookFromLib
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListContent(
    isLoading: Boolean,
    error: String?,
    searchedData: List<AnimatedItem<Book>>,
    onBookAdded: ((AnimatedItem<Book>) -> Unit)? = null,
    onBookRemoved: ((AnimatedItem<Book>) -> Unit)? = null,
) {

    if (isLoading || error != null) {
        BooksriverLoadingOrError(
            modifier = Modifier.fillMaxSize(),
            isLoading = isLoading,
            error = error
        )
    } else {
        ListOrEmptyScreen(
            isEmpty = searchedData.isEmpty(),
            emptyModifier = Modifier.fillMaxSize(),
            hasScrollState = true,
        ) {
            LazyColumn {
                items(searchedData, key = { it.book.id }) { item ->
                    AnimatedVisibility(
                        item.visible,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        BookCard(
                            item = item.book,
                            isAdd = onBookAdded != null,
                            onClick = {
                                if (onBookAdded != null) onBookAdded(item)
                                else if (onBookRemoved != null) onBookRemoved(item)
                            }
                        )
                    }
                }
            }
        }
    }

}


@Composable
fun SearchField(text: String, onSearchTextChanged: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingMedium),
        value = text,
        onValueChange = onSearchTextChanged,
        placeholder = {
            BooksriverCaption(
                text = stringResource(R.string.search),
                fontSize = 15.sp
            )
        },
        shape = RoundedCornerShape(36.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.textCaptionColor,
            backgroundColor = MaterialTheme.colors.cardBackgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.textPrimaryColor
        ),
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = { onSearchTextChanged("") }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_text_icon_description)
                    )
                }
            }
        }
    )
}

@Composable
fun BookCard(item: Book, isAdd: Boolean, onClick: (Int) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(bottom = PaddingLarge, start = HorizontalPadding, end = HorizontalPadding)
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(8.dp)),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            BookImage(
                modifier = Modifier
                    .height(64.dp)
                    .width(96.dp),
                imageUrl = item.imageUrl,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(PaddingMedium)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                BooksriverTitle1(text = item.title, fontSize = 15.sp)
                if (item.authors.isNotEmpty()) {
                    BooksriverTitle2(text = item.authors.first().name, fontSize = 13.sp)
                }
            }
            IconButton(onClick = { onClick(item.id) }) {
                Icon(
                    if (isAdd) Icons.Filled.AddCircle else Icons.Filled.RemoveCircle,
                    stringResource(R.string.add_to_library),
                    tint = MaterialTheme.colors.iconColor,
                )
            }

        }
    }
}