package com.example.booksriver.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.booksriver.theme.HorizontalPadding
import com.example.booksriver.theme.textPrimaryColor


@Composable
fun BooksriverLinearProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(horizontal = HorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator(color = MaterialTheme.colors.textPrimaryColor)
    }
}

@Composable
fun BooksriverCircularProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(horizontal = HorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.textPrimaryColor)
    }
}

@Composable
fun BooksriverLoadingOrError(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    error: String?,
    isCircularLoadingType: Boolean = true
) {
    if (isLoading) {
        if (isCircularLoadingType) {
            BooksriverCircularProgressIndicator(modifier = modifier)
        } else {
            BooksriverLinearProgressIndicator(modifier = modifier)
        }
    }
    if (error != null) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            BooksriverCaption(text = error, maxLines = Int.MAX_VALUE)
        }
    }
}