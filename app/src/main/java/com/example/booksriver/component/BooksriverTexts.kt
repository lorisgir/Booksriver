package com.example.booksriver.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.booksriver.theme.textCaptionColor
import com.example.booksriver.theme.textPrimaryColor
import com.example.booksriver.theme.textSecondaryColor

@Composable
fun BooksriverTitle1(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.textPrimaryColor,
    textAlign: TextAlign = TextAlign.Left,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip,
    fontSize: TextUnit = 17.sp
) {
    BooksriverTitle(
        text = text,
        modifier = modifier,
        color = color,
        size = fontSize,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun BooksriverTitle2(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.textSecondaryColor,
    textAlign: TextAlign = TextAlign.Left,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip,
    fontSize: TextUnit = 15.sp
) {
    BooksriverTitle(
        text = text,
        modifier = modifier,
        color = color,
        size = fontSize,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun BooksriverSubtitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.textSecondaryColor,
    textAlign: TextAlign = TextAlign.Left,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip,
    fontSize: TextUnit = 13.sp
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = TextStyle(color = color, fontWeight = FontWeight.SemiBold, fontSize = fontSize),
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun BooksriverCaption(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.textCaptionColor,
    textAlign: TextAlign = TextAlign.Left,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip,
    fontSize: TextUnit = 13.sp
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = TextStyle(color = color, fontSize = fontSize),
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun BooksriverSmallCaption(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.textCaptionColor,
    textAlign: TextAlign = TextAlign.Left,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = TextStyle(color = color, fontSize = 11.sp),
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
private fun BooksriverTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.textPrimaryColor,
    size: TextUnit = 15.sp,
    textAlign: TextAlign = TextAlign.Left,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = TextStyle(color = color, fontWeight = FontWeight.Bold, fontSize = size),
        maxLines = maxLines,
        overflow = overflow
    )
}