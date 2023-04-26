package com.example.booksriver.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.example.booksriver.R
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Book
import com.example.booksriver.theme.AllColors
import com.example.booksriver.theme.PaddingLarge
import com.example.booksriver.theme.PaddingMedium
import com.example.booksriver.theme.PaddingSmall
import kotlin.math.roundToInt


@Preview
@Composable
fun BookImage(
    modifier: Modifier = Modifier,
    imageUrl: String? = "",
    contentScale: ContentScale = ContentScale.FillBounds,
    isBordered: Boolean = true
) {
    AsyncImage(
        model = imageUrl,
        placeholder = painterResource(R.drawable.placeholder),
        contentDescription = stringResource(R.string.book_image_icon_description),
        contentScale = contentScale,
        modifier = modifier
            .border(
                width = if (isBordered) 1.dp else 0.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .padding()
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun VerticalBookCard(
    book: Book = Book.build(),
    onFavouriteClick: (Boolean, Int) -> Unit = { _, _ -> },
    onNavigateToBookDetail: (Int) -> Unit = {},
    onNavigateToBooksScreen: (String, Int) -> Unit = { _, _ -> }
) {
    val isFavourite = remember {
        mutableStateOf(book.favourite)
    }
    Card(
        modifier = Modifier
            .padding(bottom = PaddingMedium)
            .fillMaxWidth()
            .height(140.dp)
            .background(color = MaterialTheme.colors.background),
        shape = RoundedCornerShape(8.dp),
        onClick = { onNavigateToBookDetail(book.id) },
        elevation = 0.dp
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (image, text, icon) = createRefs()
            BookImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    },
                imageUrl = book.imageUrl
            )
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(text) {
                        start.linkTo(image.end)
                        end.linkTo(icon.start, margin = PaddingLarge)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = PaddingMedium)
                    .fillMaxHeight(),
            ) {
                val (box1, box2) = createRefs()

                Column(modifier = Modifier.constrainAs(box1) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = PaddingMedium)
                }) {
                    BooksriverTitle2(text = book.title, overflow = TextOverflow.Ellipsis)
                    if (book.authors.isNotEmpty()) {
                        BooksriverSubtitle(
                            text = book.authors.first().name,
                            modifier = Modifier.padding(top = PaddingSmall),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Column(modifier = Modifier.constrainAs(box2) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom, margin = PaddingMedium)
                }) {

                    Row(modifier = Modifier.padding(top = PaddingSmall)) {
                        if (!book.userBook.isAdded || book.userBook.pagesRead == 0) {
                            BooksriverSmallCaption(text = "${book.pageCount} ${stringResource(R.string.pages)}")
                        } else {
                            IconText(
                                icon = Icons.Outlined.Circle,
                                text = "${(book.userBook.percentage * 100).roundToInt()}%"
                            )
                            IconText(
                                modifier = Modifier.padding(start = PaddingMedium),
                                icon = Icons.Default.ContentCopy,
                                text = "${book.userBook.pagesRead}/${book.pageCount}"
                            )
                        }
                    }


                    Row(modifier = Modifier.padding(top = PaddingSmall)) {
                        book.categories.forEach {
                            TextChip(
                                title = it.name,
                                color = it.color,
                                colorDark = it.colorDark,
                                isSmall = true,
                                onClicked = {
                                    onNavigateToBooksScreen(
                                        K.ListBookType.CATEGORY.type, it.id
                                    )
                                })
                        }
                    }
                }


            }
            Box(modifier = Modifier
                .fillMaxHeight()
                .constrainAs(icon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }) {
                IconButton(onClick = {
                    onFavouriteClick(!isFavourite.value, book.id)
                    isFavourite.value = !isFavourite.value
                }) {
                    Icon(
                        imageVector = if (isFavourite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.favourtine_icon_description),
                        modifier = Modifier.size(25.dp),
                        tint = Color(0xFFFF6F60)
                    )
                }

            }
        }

    }
}

@Preview
@Composable
fun HorizontalBookCard(book: Book = Book.build(), onNavigateToBookDetail: (Int) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(end = PaddingMedium)
            .background(color = MaterialTheme.colors.background),
        elevation = 0.dp
    ) {
        Column() {
            BookImage(
                modifier = Modifier
                    .height(170.dp)
                    .width(120.dp)
                    .clickable { onNavigateToBookDetail(book.id) },
                imageUrl = book.imageUrl
            )
            Row(
                modifier = Modifier.padding(top = PaddingSmall),
                verticalAlignment = Alignment.Bottom
            ) {
                if (!book.userBook.isAdded || book.userBook.pagesRead == 0) {
                    BooksriverSmallCaption(text = "${book.pageCount} ${stringResource(R.string.pages)}")
                } else {
                    IconText(
                        icon = Icons.Outlined.Circle,
                        text = "${(book.userBook.percentage * 100).roundToInt()}%"
                    )
                    IconText(
                        modifier = Modifier.padding(start = PaddingMedium),
                        icon = Icons.Default.ContentCopy,
                        text = "${book.userBook.pagesRead}/${book.pageCount}"
                    )
                }
            }
        }

    }
}


@Composable
fun TextChip(
    title: String = "Testo",
    isSelected: Boolean = true,
    onClicked: () -> Unit = {},
    isSmall: Boolean = false,
    color: Int? = null,
    colorDark: Int? = null
) {
    val _color = if (!isSystemInDarkTheme()) color ?: AllColors.BLACK else colorDark ?: color
    ?: AllColors.WHITE
    val bgColor = Color(_color).copy(alpha = 0.2f)
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 2.dp, bottom = 2.dp, end = 4.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else bgColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSelected) bgColor else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable { onClicked() }
            .padding(4.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(
                horizontal = if (isSmall) 5.dp else 10.dp,
                vertical = if (isSmall) 1.dp else 3.dp
            ),
            style = TextStyle(
                color = Color(_color),
                fontWeight = FontWeight.SemiBold,
                fontSize = if (isSmall) 10.sp else 13.sp
            )
        )
    }
}
