package com.example.booksriver.ui.follow

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.K
import com.example.booksriver.data.model.User
import com.example.booksriver.theme.PaddingLarge
import com.example.booksriver.theme.PaddingMedium
import com.example.booksriver.theme.cardBackgroundColor
import com.example.booksriver.theme.iconColor
import com.example.booksriver.util.collectState

@Composable
fun FollowScreen(
    viewModel: FollowViewModel,
    onNavigateToProfileScreen: (Int) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.collectState()

    FollowContent(
        followType = state.followType,
        isLoading = state.isLoading,
        error = state.error,
        users = state.users,
        onNavigateToProfileScreen = onNavigateToProfileScreen,
        onNavigateBack = onNavigateBack
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FollowContent(
    followType: K.ListFollowType?,
    isLoading: Boolean,
    error: String?,
    users: List<User>,
    onNavigateToProfileScreen: (Int) -> Unit,
    onNavigateBack: () -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BooksriverTitle1(
                        text = if (followType == K.ListFollowType.FOLLOWERS) stringResource(R.string.followers) else stringResource(
                            R.string.following
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.TwoTone.ArrowBack,
                            contentDescription = stringResource(R.string.go_back),
                            tint = MaterialTheme.colors.iconColor
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.size(width = 70.dp, height = 50.dp),
                    )
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
    ) {
        Surface {
            if (isLoading || error != null) {
                BooksriverLoadingOrError(
                    modifier = Modifier.fillMaxSize(),
                    isLoading = isLoading,
                    error = error
                )
                return@Surface
            }

            FollowData(
                users = users,
                onNavigateToProfileScreen = onNavigateToProfileScreen
            )

        }
    }
}

@Composable
fun FollowData(
    users: List<User>,
    onNavigateToProfileScreen: (Int) -> Unit,
) {
    ListOrEmptyScreen(
        isEmpty = users.isEmpty(),
        emptyModifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = PaddingLarge)) {
            items(users) {
                UserItem(
                    user = it,
                    onNavigateToProfileScreen = onNavigateToProfileScreen
                )
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onNavigateToProfileScreen: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PaddingMedium)
            .clip(RoundedCornerShape(18.dp))
            .background(color = MaterialTheme.colors.cardBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToProfileScreen(
                        user.id
                    )
                }
                .padding(PaddingMedium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_pic),
                contentDescription = stringResource(R.string.profile_pic_icon_description),
                modifier = Modifier
                    .size(48.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            BooksriverTitle2(text = user.username)
        }
    }
}
