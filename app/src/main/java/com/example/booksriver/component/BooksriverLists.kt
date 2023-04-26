package com.example.booksriver.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.booksriver.R
import com.example.booksriver.util.isLandscapeOrTablet
import kotlin.math.ceil


@Composable
fun <E : Any> VerticalResponsiveList(
    item: @Composable (E) -> Unit,
    data: List<E>,
    isLazy: Boolean = true,
    gridCountPortrait: Int = 1,
    gridCountLandscape: Int = 2,
    messageIfEmpty: Boolean = false
) {

    val gridCount =
        if (!isLandscapeOrTablet(LocalConfiguration.current)) gridCountPortrait else gridCountLandscape

    ListOrEmptyScreen(
        isEmpty = data.isEmpty() && messageIfEmpty,
        emptyModifier = Modifier.fillMaxSize()
    ) {
        if (gridCount == 1) {
            if (isLazy) {
                LazyColumn {
                    items(data) { book ->
                        item(book)
                    }
                }
            } else {
                Column() {
                    data.forEach { book ->
                        item(book)
                    }
                }
            }
        } else {
            if (isLazy) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridCount),
                    content = {
                        items(data) { book ->
                            item(book)
                        }
                    })
            } else {
                if (data.isNotEmpty()) {
                    val chunks = data.chunked(
                        ceil(data.size.toFloat() / gridCount.toFloat()).toInt().coerceAtLeast(1)
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        chunks.forEach { chunk ->
                            Surface(modifier = Modifier.weight(1f)) {
                                Column() {
                                    chunk.forEach { book ->
                                        item(book)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ListOrEmptyScreen(
    isEmpty: Boolean,
    emptyModifier: Modifier = Modifier,
    imageSize: Dp = 200.dp,
    text: @Composable () -> Unit = { BooksriverTitle1(text = stringResource(R.string.empty)) },
    hasScrollState: Boolean = true,
    content: @Composable () -> Unit,
) {

    if (isEmpty) {
        Column(
            modifier = if (hasScrollState) emptyModifier
                .verticalScroll(rememberScrollState()) else emptyModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_list),
                contentDescription = stringResource(R.string.empty_icon_description),
                modifier = Modifier.size(imageSize)
            )
            text()
        }
    } else {
        content()
    }
}