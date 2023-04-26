package com.example.booksriver.ui.review

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.model.Review
import com.example.booksriver.theme.*
import com.example.booksriver.ui.review.write_review.WriteReviewScreen
import com.example.booksriver.util.collectState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel, isLandscape: Boolean = false, onNavigateBack: () -> Unit
) {
    val state by viewModel.collectState()

    val scope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val openSheet: () -> Unit = {
        scope.launch { bottomState.animateTo(ModalBottomSheetValue.Expanded) }
    }
    val closeSheet: () -> Unit = {
        scope.launch { bottomState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            WriteReviewScreen(
                viewModel = hiltViewModel(),
                onWriteReviewConfirmedSuccess = { type, review ->
                    viewModel.onWriteReviewConfirmedSuccess(type, review)
                    closeSheet()
                },
                onWriteReviewConfirmedError = { error ->
                    viewModel.onWriteReviewConfirmedError(error)
                    closeSheet()
                },
                onWriteReviewDismissed = closeSheet,
                onWriteReviewConfirmed = {
                    viewModel.onWriteReviewConfirmed()
                    closeSheet()
                },
                onWriteReviewInit = viewModel::onWriteReviewInit
            )
        },
        scrimColor = MaterialTheme.colors.scrimColor,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
    ) {
        SwipeRefreshWrapper(isRefreshing = state.isRefreshing, onRefresh = viewModel::refresh) {
            ReviewContent(
                isLoading = state.isLoading,
                error = state.error,
                hasPersonalReview = state.hasPersonalReview,
                reviews = state.reviews,
                isLandscape = isLandscape,
                onNavigateBack = onNavigateBack,
                openSheet = openSheet
            )
        }

    }

    if (state.toastMessage != null) {
        Toast.makeText(LocalContext.current, state.toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReviewContent(
    isLoading: Boolean,
    error: String?,
    reviews: List<Review>,
    isLandscape: Boolean,
    hasPersonalReview: Boolean?,
    onNavigateBack: () -> Unit,
    openSheet: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                BooksriverTitle1(
                    text = stringResource(R.string.reviews),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }, navigationIcon = {
                if (!isLandscape) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.TwoTone.ArrowBack,
                            contentDescription = stringResource(R.string.go_back),
                            tint = MaterialTheme.colors.iconColor
                        )
                    }
                }
            }, actions = {
                Box(
                    modifier = Modifier.size(width = 70.dp, height = 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasPersonalReview != null) {
                        IconButton(onClick = openSheet) {
                            Icon(
                                imageVector = if (hasPersonalReview) Icons.Default.EditNote else Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_or_edit_icon_description),
                                tint = MaterialTheme.colors.iconColor
                            )
                        }
                    }

                }
            }, backgroundColor = Color.Transparent, elevation = 0.dp
            )
        },
    ) {
        Surface() {
            if (isLoading || error != null) {
                BooksriverLoadingOrError(
                    modifier = Modifier.fillMaxSize(), isLoading = isLoading, error = error
                )
                return@Surface
            }
            ReviewData(reviews)
        }
    }


}


@Composable
fun ReviewData(reviews: List<Review>) {
    ListOrEmptyScreen(reviews.isEmpty(), emptyModifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(horizontal = com.example.booksriver.theme.HorizontalPadding)) {
            items(reviews) {
                ReviewCard(it)
            }
        }
    }
}

@Preview
@Composable
fun ReviewCard(review: Review = Review.build()) {
    Card(
        modifier = Modifier
            .padding(bottom = PaddingMedium)
            .fillMaxWidth()
            .padding(vertical = PaddingSmall),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        elevation = 0.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.profile_pic),
                        contentDescription = stringResource(R.string.profile_pic_icon_description),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                    BooksriverSubtitle(
                        text = review.user.username,
                        modifier = Modifier.padding(start = PaddingMedium),
                        fontSize = 15.sp
                    )
                }
                Box(
                    modifier = Modifier.padding(end = PaddingMedium),
                ) {
                    IconText(
                        icon = Icons.Default.Star, text = "${review.score}/5"
                    )
                }

            }
            BooksriverCaption(
                text = review.text,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(top = PaddingMedium)
                    .fillMaxWidth(),
                fontSize = 15.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewReviewContent() {
    ReviewContent(
        isLoading = false, error = null, emptyList(), false, true, {}, {}
    )
}