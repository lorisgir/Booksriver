package com.example.booksriver.ui.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.booksriver.R
import com.example.booksriver.component.BooksriverTitle1
import com.example.booksriver.component.BooksriverTitle2
import com.example.booksriver.data.K
import com.example.booksriver.theme.PaddingMedium
import com.example.booksriver.theme.PaddingXLarge
import com.example.booksriver.theme.buttonColor
import com.example.booksriver.util.isLandscape
import com.example.booksriver.util.isLandscapeOrTablet
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun PresentationScreen(onNavigateToMainActivity: () -> Unit = {}) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (image, title, button, center, topSpacer, bottomSpacer) = createRefs()
        val infiniteTransition = rememberInfiniteTransition()
        val offsetAnimation by infiniteTransition.animateValue(
            initialValue = (-100).dp,
            targetValue = 0.dp,
            typeConverter = Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(7500, easing = LinearEasing, delayMillis = 0),
                repeatMode = RepeatMode.Reverse
            )
        )
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .constrainAs(image) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }) {
            Image(
                painter = painterResource(id = R.drawable.books_cover_background),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height((LocalConfiguration.current.screenHeightDp + 300).dp)
                    .absoluteOffset(y = offsetAnimation),
                contentScale = if (isLandscape(LocalConfiguration.current)) ContentScale.FillWidth else ContentScale.FillHeight,
            )
        }

        Surface(modifier = Modifier.fillMaxSize(),
            color = Color.Black.copy(alpha = 0.6f),
            content = {})
        Spacer(modifier = Modifier
            .fillMaxHeight(0.1f)
            .constrainAs(topSpacer) {
                top.linkTo(parent.top)
            }
        )
        BooksriverTitle1(
            text = stringResource(R.string.app_name), modifier = Modifier
                .constrainAs(title) {
                    centerHorizontallyTo(parent)
                    top.linkTo(topSpacer.bottom)
                },
            fontSize = 32.sp, color = Color.White
        )

        Box(modifier = Modifier
            .background(color = Color.Transparent)
            .constrainAs(center) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                var index by remember { mutableStateOf(0) }
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(5000)
                        index = (index + 1) % K.splashHint.size
                    }
                }
                val animationMillis = 1000
                val delayMillis = 500
                AnimatedContent(
                    targetState = index,
                    transitionSpec = {
                        fadeIn(
                            animationSpec = tween(
                                animationMillis,
                                delayMillis = delayMillis
                            )
                        ) + scaleIn(
                            initialScale = 0.92f,
                            animationSpec = tween(animationMillis, delayMillis = delayMillis)
                        ) with fadeOut(animationSpec = tween(delayMillis))
                    }
                ) {
                    IconText(it)
                }
            }
        }



        Button(
            onClick = onNavigateToMainActivity,
            modifier = Modifier
                .constrainAs(button) {
                    bottom.linkTo(bottomSpacer.top)
                    centerHorizontallyTo(parent)
                },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.buttonColor)
        ) {
            BooksriverTitle2(
                text = stringResource(R.string.try_it),
                color = Color.White,
                modifier = Modifier.padding(horizontal = PaddingXLarge)
            )

        }
        Spacer(modifier = Modifier
            .fillMaxHeight(0.1f)
            .constrainAs(bottomSpacer) {
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
fun IconText(index: Int) {
    Column(
        modifier = Modifier
            .background(color = Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = K.splashHint[index].icon,
            contentDescription = "",
            modifier = Modifier
                .size(64.dp)
                .clip(
                    CircleShape
                )
                .background(color = Color(0XFF212121))
                .padding(PaddingMedium),
            tint = Color.White
        )
        BooksriverTitle1(
            text = stringResource(K.splashHint[index].textId),
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 22.sp
        )
    }
}
