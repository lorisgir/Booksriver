package com.example.booksriver.component

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksriver.R
import com.example.booksriver.data.BooksriverDropdownMenuItem
import com.example.booksriver.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay


@Composable
fun ConnectivityStatus(isConnected: Boolean) {
    var visibility by remember { mutableStateOf(!isConnected) }

    LaunchedEffect(isConnected) {
        visibility = if (!isConnected) {
            true
        } else {
            delay(2000)
            false
        }
    }

    AnimatedVisibility(
        visible = visibility,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        ConnectivityStatusBox(isConnected = isConnected)
    }
}

@Composable
fun EmptySearchScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.searching),
            contentDescription = stringResource(R.string.search),
            modifier = Modifier.size(200.dp)
        )
        BooksriverTitle1(text = stringResource(R.string.search_label_alt))
    }
}

@Composable
fun TopAppBarIconPlaceholder() {
    Box(modifier = Modifier.size(width = 70.dp, height = 50.dp))
}

@Composable
fun BooksriverDropdownMenu(
    items: List<BooksriverDropdownMenuItem>
) {
    val isOpen = remember {
        mutableStateOf(false)
    }
    val toggle = {
        isOpen.value = !isOpen.value
    }
    IconButton(onClick = toggle) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.more_icon_description),
            tint = MaterialTheme.colors.iconColor,
        )
    }
    DropdownMenu(
        expanded = isOpen.value,
        onDismissRequest = toggle,
    ) {
        items.forEach {
            DropdownMenuItem(onClick = {
                it.callback()
                toggle()
            }) {
                BooksriverCaption(
                    text = it.text,
                    color = MaterialTheme.colors.textPrimaryColor,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun ConnectivityStatusBox(isConnected: Boolean) {
    val backgroundColor by animateColorAsState(Color(if (isConnected) AllColors.GREEN[7] else AllColors.RED[7]))
    val message =
        if (isConnected) stringResource(R.string.back_online) else stringResource(R.string.no_internet_connection)
    val icon = if (isConnected) Icons.Default.CloudDone else Icons.Default.CloudOff

    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                stringResource(R.string.connectivity_icon_description),
                tint = Color.White
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(message, color = Color.White, fontSize = 15.sp)
        }
    }
}


@Composable
fun keyboardAsState(): State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = keypadHeight > screenHeight * 0.15
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}


@Composable
fun IconText(modifier: Modifier = Modifier, icon: ImageVector, text: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.Bottom) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.icon),
            modifier = Modifier
                .padding(end = PaddingXSmall)
                .size(13.dp),
            tint = MaterialTheme.colors.textCaptionColor
        )
        BooksriverSmallCaption(text = text)
    }
}


@Composable
fun SwipeRefreshWrapper(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
        indicator = { iState, trigger ->
            SwipeRefreshIndicator(
                state = iState,
                refreshTriggerDistance = trigger,
                scale = true,
                backgroundColor = MaterialTheme.colors.cardBackgroundColor,
                contentColor = MaterialTheme.colors.primary
            )
        }
    ) {
        content()
    }
}


@Composable
fun HorizontalFadingEdge(modifier: Modifier = Modifier, isVisible: Boolean, topToBottom: Boolean) {
    if (isVisible) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (topToBottom) listOf(
                            Color.Transparent, MaterialTheme.colors.background
                        ) else listOf(
                            MaterialTheme.colors.background, Color.Transparent
                        )
                    )
                )
        )
    }
}
