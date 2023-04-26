package com.example.booksriver.ui.book

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.booksriver.R
import com.example.booksriver.SplashActivity
import com.example.booksriver.component.*
import com.example.booksriver.component.scrollbar.Carousel
import com.example.booksriver.component.scrollbar.CarouselDefaults
import com.example.booksriver.component.scrollbar.rememberCarouselScrollState
import com.example.booksriver.component.scrollbar.verticalScroll
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Book
import com.example.booksriver.data.model.Category
import com.example.booksriver.data.model.UserBook
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.theme.*
import com.example.booksriver.ui.components.userlibrary_dialog.UserLibraryDialog
import com.example.booksriver.util.collectState
import com.example.booksriver.util.createNotification
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookScreen(
    viewModel: BookViewModel,
    splitted: Boolean = false,
    onNavigateBack: () -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    onNavigateToReviewScreen: (Int) -> Unit
) {
    val state by viewModel.collectState()
    val scope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val openSheet: (BottomSheetScreen) -> Unit = {
        viewModel.onBottomSheetChanged(it)
        scope.launch { bottomState.show() }
    }

    BottomSheetWrapper(
        state = state,
        viewModel = viewModel,
        scope = scope,
        bottomState = bottomState
    ) {
        SwipeRefreshWrapper(isRefreshing = state.isRefreshing, onRefresh = viewModel::refresh) {
            BookContent(
                state = state,
                viewModel = viewModel,
                splitted = splitted,
                onNavigateBack = onNavigateBack,
                onNavigateToBooksScreen = onNavigateToBooksScreen,
                onNavigateToReviewScreen = onNavigateToReviewScreen,
                openSheet = openSheet
            )
        }
    }

    if (state.toastMessage != null) {
        Toast.makeText(LocalContext.current, state.toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }

    if (state.isCreateLibraryDialogVisible) {
        UserLibraryDialog(
            viewModel = hiltViewModel(),
            onDismissed = viewModel::toggleCreateLibraryDialog,
            onConfirmed = viewModel::onUserLibraryCreated,
            onConfirmedError = viewModel::onUserLibraryCreatedError
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetWrapper(
    state: BookState,
    viewModel: BookViewModel,
    scope: CoroutineScope,
    bottomState: ModalBottomSheetState,
    content: @Composable () -> Unit
) {

    val closeSheet: () -> Unit = {
        viewModel.onBottomSheetChanged(null)
        scope.launch { bottomState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = bottomState, sheetContent = {
            Column(
                modifier = Modifier
                    .padding(horizontal = HorizontalPadding, vertical = PaddingSmall)
                    .fillMaxWidth()
            ) {
                when (state.bottomSheetScreen) {
                    BottomSheetScreen.StatusBottomSheet -> StatusBottomSheet(
                        state.userBook,
                        viewModel::onReadingStatusChanged,
                        viewModel::removeBook,
                        closeSheet
                    )
                    BottomSheetScreen.LibraryBottomSheet -> LibraryBottomSheet(
                        userLibraries = state.userLibraries,
                        bookUserLibraries = state.bookUserLibraries,
                        isLoading = state.userLibrariesLoading && state.userLibrariesLoading2,
                        error = state.uerLibrariesError,
                        onUserLibraryClicked = viewModel::onUserLibraryClicked,
                        onToggleCreateLibraryDialog = viewModel::toggleCreateLibraryDialog
                    )
                    else -> Box {}
                }
            }
        },
        scrimColor = MaterialTheme.colors.scrimColor,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        content()
    }

}


@Composable
fun BookContent(
    state: BookState,
    viewModel: BookViewModel,
    splitted: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    openSheet: (BottomSheetScreen) -> Unit,
    onNavigateToReviewScreen: (Int) -> Unit
) {
    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            BookTopBar(
                bookId = state.book?.id,
                title = state.book?.title ?: stringResource(R.string.book),
                onNavigateBack = onNavigateBack,
                openSheet = openSheet
            )
        },
        bottomBar = {
            if (!state.isLoading && state.error == null && state.book != null) {
                BookBottomBar(
                    isFavourite = state.book.favourite,
                    userBook = state.userBook,
                    onAddUserBook = viewModel::addReadingUserBook,
                    onFavouriteClick = viewModel::onFavouriteClick,
                    openSheet = openSheet
                )
            }
        },
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            if (state.isLoading || state.error != null) {
                BooksriverLoadingOrError(
                    modifier = Modifier.fillMaxSize(),
                    isLoading = state.isLoading,
                    error = state.error
                )
                return@Surface
            }
            if (state.book != null) {
                BookDetailContent(
                    state = state,
                    viewModel = viewModel,
                    splitted = splitted,
                    onNavigateToBooksScreen = onNavigateToBooksScreen,
                    onNavigateToReviewScreen = onNavigateToReviewScreen
                )
            }
        }

    }

}


@Composable
fun StatusBottomSheet(
    userBook: UserBook,
    onReadingStatusChanged: (K.BookStatus) -> Unit,
    onRemoveBook: () -> Unit,
    closeSheet: () -> Unit
) {
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BooksriverTitle1(text = stringResource(R.string.change_reading_status))
            TextButton(onClick = {
                onRemoveBook()
                closeSheet()
            }) {
                BooksriverCaption(text = stringResource(R.string.remove_book))
            }

        }
        Column(modifier = Modifier.padding(top = PaddingLarge)) {
            K.BookStatus.values().forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (item == userBook.status),
                        onClick = {
                            onReadingStatusChanged(item)
                            closeSheet()
                        }
                    )
                    TextChip(
                        title = stringResource(item.titleId),
                        color = item.color,
                        onClicked = {
                            onReadingStatusChanged(item)
                            closeSheet()
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun LibraryBottomSheet(
    userLibraries: List<UserLibrary>,
    bookUserLibraries: List<UserLibrary>,
    isLoading: Boolean,
    error: String?,
    onUserLibraryClicked: (UserLibrary, Boolean) -> Unit,
    onToggleCreateLibraryDialog: () -> Unit,
) {
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BooksriverTitle1(text = stringResource(R.string.add_to_library))
            TextButton(onClick = onToggleCreateLibraryDialog) {
                BooksriverSubtitle(text = stringResource(R.string.create_new))
            }
        }
        if (isLoading || error != null) {
            BooksriverLoadingOrError(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                isLoading = isLoading,
                error = error,
            )
            return@Column
        }

        ListOrEmptyScreen(
            isEmpty = userLibraries.isEmpty(),
            emptyModifier = Modifier
                .height(100.dp)
                .fillMaxWidth(),
            imageSize = 60.dp,
            text = { BooksriverCaption(text = stringResource(R.string.empty)) }) {
            FlowRow(
                modifier = Modifier.padding(vertical = PaddingLarge),
                mainAxisSpacing = PaddingMedium,
                crossAxisSpacing = PaddingMedium
            ) {
                userLibraries.forEach { item ->
                    val selected = bookUserLibraries.contains(item)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextChip(
                            title = item.name,
                            color = item.color,
                            colorDark = item.colorDark,
                            isSelected = selected,
                            onClicked = {
                                onUserLibraryClicked(item, !selected)
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun BookTopBar(
    title: String,
    bookId: Int?,
    onNavigateBack: () -> Unit,
    openSheet: (BottomSheetScreen) -> Unit
) {
    val localContext = LocalContext.current
    TopAppBar(title = {
        BooksriverTitle1(
            text = title,
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
        if (bookId != null) {

            Row {
                IconButton(onClick = {
                    val notificationIntent = Intent(localContext, SplashActivity::class.java)
                    notificationIntent.putExtra("bookId", bookId)
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    createNotification(
                        notificationIntent,
                        localContext,
                        "Pending Intent Notification Test",
                        "Click here to open book with title '$title'"
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.NotificationAdd,
                        contentDescription = stringResource(R.string.add_to_library),
                        tint = MaterialTheme.colors.iconColor
                    )
                }
                IconButton(onClick = { openSheet(BottomSheetScreen.LibraryBottomSheet) }) {
                    Icon(
                        imageVector = Icons.Default.LibraryAdd,
                        contentDescription = stringResource(R.string.add_to_library),
                        tint = MaterialTheme.colors.iconColor
                    )
                }
            }

        }
    }, backgroundColor = Color.Transparent, elevation = 0.dp
    )
}

@Composable
fun BookBottomBar(
    isFavourite: Boolean,
    userBook: UserBook,
    onAddUserBook: () -> Unit,
    onFavouriteClick: (Boolean) -> Unit,
    openSheet: (BottomSheetScreen) -> Unit,
) {
    Surface(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = HorizontalPadding, vertical = PaddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onFavouriteClick(!isFavourite) },
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.cardBackgroundColor)
            ) {
                Icon(
                    imageVector = if (isFavourite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(R.string.favourtine_icon_description),
                    modifier = Modifier.size(25.dp),
                    tint = Color(0xFFFF6F60)
                )
            }
            Spacer(modifier = Modifier.width(PaddingMedium))
            Button(
                onClick = {
                    if (userBook.status == K.BookStatus.NOT_STARTED) {
                        onAddUserBook()
                    } else {
                        openSheet(BottomSheetScreen.StatusBottomSheet)
                    }
                },
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(userBook.status.color))
            ) {
                BooksriverTitle2(
                    text = stringResource(userBook.status.buttonTextId),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun BookDetailContent(
    state: BookState,
    viewModel: BookViewModel,
    splitted: Boolean,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    onNavigateToReviewScreen: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(vertical = PaddingMedium, horizontal = HorizontalPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookHeader(state.book!!, onNavigateToBooksScreen)
        BookInfo(state.book, splitted, onNavigateToReviewScreen)
        PagesSlider(state = state, viewModel = viewModel)
        BookCategories(state.book.categories, onNavigateToBooksScreen)
        BookDescription(state.book)
    }
}

@Composable
fun PagesSlider(
    state: BookState,
    viewModel: BookViewModel,
) {
    if (state.userBook.isAdded) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    modifier = Modifier.weight(1f),
                    value = state.sliderPages,
                    valueRange = 0f..state.book!!.pageCount.toFloat(),
                    onValueChange = viewModel::onSliderPagesChanged,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colors.buttonColor,
                        activeTrackColor = MaterialTheme.colors.scrollBarColor
                    ),
                    onValueChangeFinished = viewModel::onSliderPagesEndDrag
                )
                IconButton(onClick = { viewModel.onDialogPagesVisible() }) {
                    Icon(
                        imageVector = Icons.Default.Pin,
                        contentDescription = stringResource(R.string.pages_icon_description),
                        tint = MaterialTheme.colors.iconColor
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                BooksriverCaption(
                    text = "${state.userBook.pagesRead}/${state.book!!.pageCount} | ${(state.userBook.percentage * 100).roundToInt()}% ${
                        stringResource(
                            R.string.completed
                        )
                    }"
                )
            }

        }
        if (state.isDialogPagesVisible) {
            ConfirmDismissDialog(
                title = stringResource(R.string.change_pages_read),
                onConfirmed = viewModel::onDialogPagesConfirmed,
                onDismissed = viewModel::onDialogPagesDismissed
            ) {
                BooksriverTextField(value = state.inputPages,
                    onValueChange = viewModel::onInputPagesChanged,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    label = stringResource(R.string.pages_read),
                    isError = state.isValidInputPages == false,
                    onAction = KeyboardActions {
                        viewModel.onDialogPagesConfirmed()
                    })
            }
        }
    }
}


@Composable
private fun BookHeader(book: Book, onNavigateToBooksScreen: (String, Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        BookImage(
            modifier = Modifier
                .height(200.dp)
                .width(140.dp), imageUrl = book.imageUrl
        )
        if (book.authors.isNotEmpty()) {
            val author = book.authors.first()
            TextButton(onClick = {
                onNavigateToBooksScreen(
                    K.ListBookType.AUTHOR.type, author.id
                )
            }) {
                BooksriverTitle1(
                    text = "${stringResource(R.string.author)}: ${author.name}",
                    fontSize = 13.sp
                )
            }
        }
    }

}

@Composable
private fun BookInfo(book: Book, splitted: Boolean, onNavigateToReviewScreen: (Int) -> Unit) {
    Surface(
        modifier = Modifier.padding(horizontal = PaddingLarge, vertical = PaddingMedium),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colors.cardBackgroundColor),

            ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BookInfoItem(
                    modifier = Modifier.weight(1f), icon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(R.string.star_icon_description),
                            modifier = Modifier.size(15.dp),
                            tint = Color(0xFFE9C867)
                        )
                    },
                    primaryText = book.averageRating.toString(),
                    secondaryText = "/5",
                    onNavigateToReviewScreen = { if (!splitted) onNavigateToReviewScreen(book.id) }
                )
                BookInfoVerticalDivider()
                BookInfoItem(
                    modifier = Modifier.weight(1f),
                    primaryText = book.ratingsCount.toString(),
                    secondaryText = stringResource(R.string.reviews_short),
                    onNavigateToReviewScreen = { if (!splitted) onNavigateToReviewScreen(book.id) }
                )
                BookInfoVerticalDivider()
                BookInfoItem(
                    modifier = Modifier.weight(1f),
                    primaryText = book.pageCount.toString(),
                    secondaryText = stringResource(R.string.pages),
                    onNavigateToReviewScreen = {}
                )

            }
        }
    }
}

@Composable
fun BookInfoVerticalDivider() {
    Divider(
        color = Color.LightGray,
        modifier = Modifier
            .padding(vertical = PaddingLarge)
            .fillMaxHeight()
            .width(1.dp)
    )
}

@Composable
fun BookInfoItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},
    primaryText: String,
    secondaryText: String,
    onNavigateToReviewScreen: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onNavigateToReviewScreen() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        icon()
        Row(
            modifier = Modifier.padding(start = PaddingSmall), verticalAlignment = Alignment.Bottom
        ) {
            BooksriverTitle1(text = primaryText)
            BooksriverCaption(text = " $secondaryText")
        }
    }

}

@Composable
private fun BookCategories(
    categories: List<Category>,
    onNavigateToBooksScreen: (String, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = PaddingLarge, bottom = PaddingLarge),
        horizontalAlignment = Alignment.Start
    ) {
        BooksriverTitle1(
            text = stringResource(R.string.categories),
            modifier = Modifier.padding(bottom = PaddingMedium)
        )
        LazyRow {
            items(categories) {
                TextChip(title = it.name,
                    isSmall = true,
                    color = it.color,
                    colorDark = it.colorDark,
                    onClicked = { onNavigateToBooksScreen(K.ListBookType.CATEGORY.type, it.id) })
            }
        }
    }
}


@Composable
private fun BookDescription(book: Book) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start
    ) {
        BooksriverTitle1(
            text = stringResource(R.string.synopsis),
            modifier = Modifier.padding(bottom = PaddingMedium)
        )

        if (book.description != null) {
            BooksriverCaption(
                text = book.description, textAlign = TextAlign.Justify, maxLines = Int.MAX_VALUE
            )
        } else {
            BooksriverSubtitle(text = stringResource(R.string.no_description))
        }

    }
}

@Composable
private fun BookDescriptionScrollable(book: Book) {
    val scrollState = rememberCarouselScrollState()

    Row() {
        Carousel(
            state = scrollState,
            modifier = Modifier
                .padding(PaddingLarge)
                .width(4.dp)
                .fillMaxHeight(),
            colors = CarouselDefaults.colors(
                thumbColor = MaterialTheme.colors.scrollBarColor,
                backgroundColor = MaterialTheme.colors.scrollBarBackgroundColor,
            )
        )
        Column() {
            BooksriverTitle1(
                text = stringResource(R.string.synopsis),
                modifier = Modifier.padding(bottom = PaddingMedium)
            )

            ConstraintLayout(
                modifier = Modifier.weight(1f)
            ) {
                val (fadeTop, fadeBottom, text) = createRefs()
                HorizontalFadingEdge(
                    modifier = Modifier
                        .zIndex(1f)
                        .constrainAs(fadeTop) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }, isVisible = scrollState.value > 0, topToBottom = false
                )
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }) {
                    if (book.description != null) {
                        BooksriverCaption(
                            text = book.description,
                            textAlign = TextAlign.Justify,
                            maxLines = Int.MAX_VALUE
                        )
                    } else {
                        BooksriverSubtitle(text = stringResource(R.string.no_description))
                    }
                }
                HorizontalFadingEdge(
                    modifier = Modifier.constrainAs(fadeBottom) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
                    isVisible = (scrollState.maxValue - scrollState.value) != 0,
                    topToBottom = true
                )
            }

        }
    }
}
