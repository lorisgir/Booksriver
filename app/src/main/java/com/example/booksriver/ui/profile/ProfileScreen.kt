package com.example.booksriver.ui.profile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.BooksriverDropdownMenuItem
import com.example.booksriver.data.K
import com.example.booksriver.data.model.Profile
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.theme.*
import com.example.booksriver.ui.components.userlibrary_dialog.UserLibraryDialog
import com.example.booksriver.util.collectState

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToFollowScreen: (Int, String) -> Unit,
) {
    val state by viewModel.collectState()

    if (!state.isLogged) {
        onNavigateToLogin()
    }

    ProfileContent(
        isPersonalProfile = state.userId == 0,
        isLoading = state.isLoading,
        error = state.error,
        profile = state.profile,
        onChangeUsernameButtonClicked = viewModel::showUsernameDialog,
        showUsernameDialog = state.showUsernameDialog,
        newUsername = state.newUsername,
        onUsernameChange = viewModel::setUsername,
        onChangeUsernameDialogClick = viewModel::changeUsername,
        libraries = state.libraries,
        onCreateLibButtonClicked = viewModel::showCreateLibDialog,
        onEditLibNameButtonClicked = viewModel::showEditLibDialog,
        onDialogDismiss = viewModel::hideDialog,
        isValidUsername = state.isValidUsername ?: true,
        onLogoutClick = viewModel::logout,
        onLibDelete = viewModel::deleteLibrary,
        onNavigateToBooksScreen = onNavigateToBooksScreen,
        onFollowButtonClicked = viewModel::handleFollow,
        onNavigateBack = onNavigateBack,
        onNavigateToFollowScreen = onNavigateToFollowScreen,
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::refresh,
    )

    if (state.toastMessage != null) {
        Toast.makeText(LocalContext.current, state.toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetToastMessage()
    }

    if (state.showLibDialog) {
        UserLibraryDialog(
            viewModel = hiltViewModel(),
            onDismissed = viewModel::toggleCreateLibraryDialog,
            onConfirmed = viewModel::onUserLibraryCreated,
            onConfirmedError = viewModel::onUserLibraryCreatedError,
            isCreate = state.isCreateLibDialog,
            libraryName = state.libDialog?.name,
            libraryId = state.libDialog?.id,
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileContent(
    isPersonalProfile: Boolean,
    isLoading: Boolean,
    error: String?,
    profile: Profile?,
    onChangeUsernameButtonClicked: () -> Unit,
    showUsernameDialog: Boolean,
    newUsername: String,
    onUsernameChange: (String) -> Unit,
    onChangeUsernameDialogClick: () -> Unit,
    libraries: List<UserLibrary>,
    onCreateLibButtonClicked: () -> Unit,
    onDialogDismiss: () -> Unit,
    isValidUsername: Boolean,
    onLogoutClick: () -> Unit,
    onLibDelete: (UserLibrary) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    onFollowButtonClicked: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToFollowScreen: (Int, String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onEditLibNameButtonClicked: (UserLibrary) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BooksriverTitle1(
                        text = if (isPersonalProfile) stringResource(R.string.my_profile) else stringResource(
                            R.string.visit_profile
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (isPersonalProfile) TopAppBarIconPlaceholder() else IconButton(onClick = onNavigateBack) {
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
                        contentAlignment = Alignment.Center
                    ) {
                        if (isPersonalProfile) {
                            BooksriverDropdownMenu(
                                items = listOf(
                                    BooksriverDropdownMenuItem(
                                        text = stringResource(R.string.logout),
                                        callback = onLogoutClick
                                    )
                                )
                            )
                        }
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
    ) {
        SwipeRefreshWrapper(isRefreshing, onRefresh) {

            Surface {
                if (isLoading || error != null) {
                    BooksriverLoadingOrError(
                        modifier = Modifier.fillMaxSize(),
                        isLoading = isLoading,
                        error = error
                    )
                    return@Surface
                }

                ProfileData(
                    isPersonalProfile = isPersonalProfile,
                    profile = profile!!,
                    onChangeUsernameButtonClicked = onChangeUsernameButtonClicked,
                    showUsernameDialog = showUsernameDialog,
                    newUsername = newUsername,
                    onUsernameChange = onUsernameChange,
                    onChangeUsernameDialogClick = onChangeUsernameDialogClick,
                    libraries = libraries,
                    onCreateLibButtonClicked = onCreateLibButtonClicked,
                    onDialogDismiss = onDialogDismiss,
                    isValidUsername = isValidUsername,
                    onLibDelete = onLibDelete,
                    onNavigateToBooksScreen = onNavigateToBooksScreen,
                    onFollowButtonClicked = onFollowButtonClicked,
                    onNavigateToFollowScreen = onNavigateToFollowScreen,
                    onEditLibNameButtonClicked = onEditLibNameButtonClicked
                )

            }
        }
    }
}

@Composable
fun ProfileData(
    isPersonalProfile: Boolean,
    profile: Profile,
    onChangeUsernameButtonClicked: () -> Unit,
    showUsernameDialog: Boolean,
    newUsername: String,
    onUsernameChange: (String) -> Unit,
    onChangeUsernameDialogClick: () -> Unit,
    libraries: List<UserLibrary>,
    onCreateLibButtonClicked: () -> Unit,
    onDialogDismiss: () -> Unit,
    isValidUsername: Boolean,
    onLibDelete: (UserLibrary) -> Unit,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    onFollowButtonClicked: () -> Unit,
    onNavigateToFollowScreen: (Int, String) -> Unit,
    onEditLibNameButtonClicked: (UserLibrary) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = HorizontalPadding)
    ) {


        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = PaddingXLarge),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = stringResource(R.string.profile_pic_icon_description),
                    modifier = Modifier
                        .size(86.dp)
                )
                Spacer(Modifier.padding(8.dp))
                BooksriverTitle1(text = profile.username)

                if (isPersonalProfile) {
                    IconButton(onClick = onChangeUsernameButtonClicked) {
                        Icon(
                            Icons.Sharp.Edit,
                            stringResource(R.string.edit_icon_description),
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (!isPersonalProfile) {
                Button(
                    onClick = { onFollowButtonClicked() },
                    modifier = Modifier
                        .padding(bottom = PaddingMedium)
                        .height(32.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(48),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                ) {
                    BooksriverSubtitle(
                        text = stringResource(if (profile.followed) R.string.unfollow else R.string.follow),
                        color = Color.White
                    )
                }
            }
        }

        BooksriverCaption(
            text = stringResource(R.string.social),
            color = Color(0xFFAAAAAA),
            fontSize = 15.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = PaddingLarge),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SocialCard(
                value = libraries.size,
                title = stringResource(R.string.libraries),
                onClick = {}
            )
            SocialCard(
                value = profile.follower,
                title = stringResource(R.string.followers),
                onClick = { onNavigateToFollowScreen(profile.id, K.ListFollowType.FOLLOWERS.type) }
            )
            SocialCard(
                value = profile.following,
                title = stringResource(R.string.following),
                onClick = { onNavigateToFollowScreen(profile.id, K.ListFollowType.FOLLOWING.type) }
            )
        }

        BooksriverCaption(
            text = stringResource(if (isPersonalProfile) R.string.my_library else R.string.libraries),
            color = Color(0xFFAAAAAA),
            fontSize = 15.sp,
            modifier = Modifier.padding(top = PaddingLarge)
        )

        ListOrEmptyScreen(
            isEmpty = libraries.isEmpty(),
            emptyModifier = Modifier.fillMaxWidth(),
            imageSize = 100.dp,
            hasScrollState = false,
            text = { BooksriverCaption(text = stringResource(R.string.empty)) }
        ) {
            Column(modifier = Modifier.padding(vertical = PaddingLarge)) {
                libraries.forEach {
                    LibraryItem(
                        isPersonalProfile = isPersonalProfile,
                        library = it,
                        onNavigateToBooksScreen = onNavigateToBooksScreen,
                        onLibDelete = onLibDelete,
                        onLibEditName = onEditLibNameButtonClicked
                    )
                }
            }
        }



        if (isPersonalProfile) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onCreateLibButtonClicked() },
                modifier = Modifier
                    .padding(vertical = PaddingXLarge)
                    .height(32.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(48),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
            ) {
                BooksriverSubtitle(text = stringResource(R.string.create), color = Color.White)
            }


            if (showUsernameDialog) {
                ConfirmDismissDialog(
                    title = stringResource(id = R.string.change_username),
                    confirmButtonText = stringResource(id = R.string.confirm),
                    onDismissed = onDialogDismiss,
                    onConfirmed = onChangeUsernameDialogClick
                ) {
                    BooksriverTextField(
                        value = newUsername,
                        label = stringResource(R.string.username),
                        onValueChange = onUsernameChange,
                        imeAction = ImeAction.Done,
                        onAction = KeyboardActions {
                            onChangeUsernameDialogClick()
                        },
                        isError = !isValidUsername
                    )
                }
            }
        }
    }
}


@Composable
fun SocialCard(
    value: Int,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() }
                .padding(PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BooksriverSubtitle(text = value.toString())
            Spacer(Modifier.padding(4.dp))
            BooksriverTitle2(text = title)
        }
    }

}

@Composable
fun LibraryItem(
    isPersonalProfile: Boolean,
    library: UserLibrary,
    onNavigateToBooksScreen: (String, Int) -> Unit,
    onLibDelete: (UserLibrary) -> Unit,
    onLibEditName: (UserLibrary) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PaddingMedium)
            .clip(shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onNavigateToBooksScreen(
                        K.ListBookType.USER_LIBRARY.type,
                        library.id
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(if (!isSystemInDarkTheme()) library.color else library.colorDark)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.LocalLibrary,
                    stringResource(R.string.lib_icon_description),
                    tint = MaterialTheme.colors.background
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column() {
                BooksriverTitle2(text = library.name)
                BooksriverCaption(text = "${library.count} ${stringResource(id = R.string.books)}")

            }
            Spacer(modifier = Modifier.weight(1f))


            if (isPersonalProfile) {
                Box() {
                    BooksriverDropdownMenu(
                        items = listOf(
                            BooksriverDropdownMenuItem(
                                text = stringResource(R.string.edit_name),
                                callback = { onLibEditName(library) }
                            ),
                            BooksriverDropdownMenuItem(
                                text = stringResource(R.string.delete),
                                callback = { onLibDelete(library) }
                            )
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    /*ProfileContent(
        isLoading = false,
        error = null,
        profile = Profile(
            id = 0,
            username = "Marco",
            follower = 3,
            following = 4,
            followed = false,
            libraries = listOf()
        ),
        onLibButtonClicked = null
    )*/
}