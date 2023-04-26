package com.example.booksriver.component


import androidx.annotation.RawRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.booksriver.R
import com.example.booksriver.theme.PaddingLarge
import com.example.booksriver.theme.PaddingMedium
import com.example.booksriver.theme.buttonColor


@Composable
fun ConfirmDismissDialog(
    title: String,
    confirmButtonText: String = stringResource(R.string.confirm),
    onConfirmed: () -> Unit = {},
    onDismissed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissed,
    ) {
        Card(
            shape = RoundedCornerShape(22.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(vertical = PaddingLarge, horizontal = PaddingLarge)
            ) {
                BooksriverTitle1(
                    text = title,
                    modifier = Modifier.padding(top = PaddingMedium, bottom = PaddingLarge),
                    fontSize = 19.sp
                )
                content()
                Row(
                    modifier = Modifier
                        .padding(top = PaddingLarge)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissed,
                    ) {
                        BooksriverSubtitle(text = stringResource(R.string.cancel))
                    }
                    TextButton(
                        modifier = Modifier.padding(start = PaddingLarge),
                        onClick = onConfirmed,
                    ) {
                        BooksriverSubtitle(text = confirmButtonText)
                    }
                }
            }
        }
    }
}

@Composable
fun LoaderDialog() {
    Dialog(onDismissRequest = {}) {
        Surface(modifier = Modifier.size(128.dp)) {
            LottieAnimation(
                resId = R.raw.loading, modifier = Modifier
                    .padding(16.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
fun FailureDialog(failureMessage: String, onDismissed: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismissed) {
        Surface {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LottieAnimation(
                    resId = R.raw.failure, modifier = Modifier
                        .padding(0.dp)
                        .size(86.dp)
                )
                BooksriverTitle1(
                    text = failureMessage,
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = onDismissed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.buttonColor)
                ) {
                    BooksriverCaption(text = stringResource(R.string.ok), fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun LottieAnimation(
    @RawRes resId: Int,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    restartOnPlay: Boolean = true
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = iterations,
        restartOnPlay = restartOnPlay
    )

    com.airbnb.lottie.compose.LottieAnimation(composition, progress, modifier = modifier)
}