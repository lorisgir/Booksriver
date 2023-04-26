package com.example.booksriver.theme

import androidx.annotation.ColorInt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils


private val DarkColorPalette = darkColors(
    primary = PrimaryDark,
    primaryVariant = SecondaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    surface = BackgroundDark
)

private val LightColorPalette = lightColors(
    primary = PrimaryLight,
    primaryVariant = SecondaryLight,
    secondary = SecondaryLight,
    background = BackgroundLight,
    surface = BackgroundLight
)

val Colors.textPrimaryColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) PrimaryLight else Color.White

val Colors.textSecondaryColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

val Colors.textCaptionColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

val Colors.scrollBarColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) PrimaryLight else Color.DarkGray

val Colors.scrollBarBackgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.LightGray else SurfaceDark

val Colors.iconColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) PrimaryLight else Color.White

val Colors.titleColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) PrimaryLight else SecondaryDark

val Colors.buttonColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) PrimaryLight else SecondaryDark

val Colors.cardBackgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) SurfaceLight else SurfaceDark

val Colors.scrimColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) onSurface.copy(0.32f) else Color.DarkGray.copy(0.32f)


@Composable
fun BooksriverTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }



    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}