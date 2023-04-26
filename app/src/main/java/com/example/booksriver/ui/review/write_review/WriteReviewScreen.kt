package com.example.booksriver.ui.review.write_review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksriver.R
import com.example.booksriver.component.BooksriverDropdownMenu
import com.example.booksriver.component.BooksriverLoadingOrError
import com.example.booksriver.component.BooksriverTextField
import com.example.booksriver.component.BooksriverTitle1
import com.example.booksriver.data.BooksriverDropdownMenuItem
import com.example.booksriver.data.model.Review
import com.example.booksriver.theme.HorizontalPadding
import com.example.booksriver.theme.PaddingMedium
import com.example.booksriver.theme.PaddingSmall
import com.example.booksriver.theme.iconColor
import com.example.booksriver.util.collectState
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WriteReviewScreen(
    viewModel: WriteReviewViewModel,
    onWriteReviewConfirmedSuccess: (Int, Review) -> Unit,
    onWriteReviewConfirmedError: (String) -> Unit,
    onWriteReviewDismissed: () -> Unit,
    onWriteReviewConfirmed: () -> Unit,
    onWriteReviewInit: (Boolean) -> Unit,
) {
    val state by viewModel.collectState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        onWriteReviewInit(state.personalReview != null)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .background(color = MaterialTheme.colors.background)
            .padding(vertical = PaddingSmall)
    ) {
        if (state.isLoading || state.error != null) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                BooksriverLoadingOrError(
                    isLoading = state.isLoading,
                    error = state.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onWriteReviewDismissed) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.close_icon_description),
                            tint = MaterialTheme.colors.iconColor,
                        )
                    }
                    BooksriverTitle1(
                        text = if (state.personalReview == null) stringResource(R.string.add_review) else stringResource(
                            R.string.edit_review
                        ),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 17.sp
                    )
                }
                Row() {
                    IconButton(onClick = {
                        val isReviewTextValid = viewModel.onSave(
                            onWriteReviewConfirmedSuccess,
                            onWriteReviewConfirmedError
                        )
                        if (isReviewTextValid) {
                            keyboardController?.hide()
                            onWriteReviewConfirmed()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = stringResource(R.string.save),
                            tint = MaterialTheme.colors.iconColor,
                        )
                    }
                    if (state.personalReview != null) {
                        BooksriverDropdownMenu(
                            items = listOf(
                                BooksriverDropdownMenuItem(
                                    text = stringResource(R.string.delete),
                                    callback = {
                                        viewModel.onDelete(
                                            onWriteReviewConfirmedSuccess,
                                            onWriteReviewConfirmedError
                                        )
                                        onWriteReviewConfirmed()
                                    }
                                )
                            )
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HorizontalPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                FormItem(label = stringResource(R.string.score)) {
                    RatingBar(
                        value = state.score.toFloat(),
                        config = RatingBarConfig()
                            .style(RatingBarStyle.HighLighted)
                            .inactiveColor(Color.LightGray)
                            .stepSize(StepSize.HALF),
                        onValueChange = viewModel::onScoreChanged,
                        onRatingChanged = {},
                        modifier = if (state.isReviewScoreValid) Modifier else Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colors.error,
                            shape = RoundedCornerShape(4.dp)
                        )
                    )
                }
                FormItem(label = stringResource(R.string.text)) {
                    BooksriverTextField(
                        value = state.text,
                        label = stringResource(R.string.text),
                        textModifier = Modifier
                            .height(200.dp),
                        onValueChange = viewModel::onTextChanged,
                        imeAction = ImeAction.Default,
                        isError = !state.isReviewTextValid
                    )
                }
            }
        }
    }

}

@Composable
fun FormItem(label: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .padding(vertical = PaddingMedium)
            .fillMaxWidth()
    ) {
        BooksriverTitle1(
            text = label,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = PaddingSmall)
        )
        content()
    }
}