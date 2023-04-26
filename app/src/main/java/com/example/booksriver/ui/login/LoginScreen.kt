package com.example.booksriver.ui.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksriver.R
import com.example.booksriver.component.*
import com.example.booksriver.data.K
import com.example.booksriver.theme.*
import com.example.booksriver.util.collectState

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val state by viewModel.collectState()

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            viewModel.googleLogin(task)
        }

    LoginContent(
        isLoading = state.isLoading,
        username = state.username,
        password = state.password,
        isValidUsername = state.isValidUsername ?: true,
        isValidPassword = state.isValidPassword ?: true,
        onUsernameChange = viewModel::setUsername,
        onPasswordChange = viewModel::setPassword,
        onLoginClick = viewModel::login,
        onNavigateToSignUp = onNavigateToSignUp,
        error = state.error,
        showFailureDialog = state.showFailureDialog,
        onDismissFailureDialog = viewModel::dismissFailureDialog,
        onSocialLoginClick = { authResultLauncher.launch(K.SOCIAL_LOGIN_REQUEST_CODE) }
    )

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onNavigateToMain()
        }
    }
}

@Composable
fun LoginContent(
    isLoading: Boolean,
    username: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    password: String,
    onNavigateToSignUp: () -> Unit,
    onLoginClick: () -> Unit,
    isValidUsername: Boolean,
    isValidPassword: Boolean,
    error: String?,
    showFailureDialog: Boolean,
    onDismissFailureDialog: () -> Unit,
    onSocialLoginClick: () -> Unit,
) {
    if (isLoading) {
        LoaderDialog()
    }

    if (showFailureDialog && error != null) {
        FailureDialog(error, onDismissFailureDialog)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        /*Image(
            ImageVector.vectorResource(
                id = R.drawable.ic_undraw_bibliophile_re_xarc
            ),
            "Welcome image",
            modifier = Modifier
                .size(width = Dp.Infinity, height = 192.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)
        )*/

        Column() {
            BooksriverTitle1(
                text = stringResource(R.string.login),
                fontSize = 34.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 16.dp)
            )

            LoginForm(
                username = username,
                onUsernameChange = onUsernameChange,
                isValidUsername = isValidUsername,
                password = password,
                onPasswordChange = onPasswordChange,
                isValidPassword = isValidPassword,
                onLoginClick = onLoginClick,
                onSocialLoginClick = onSocialLoginClick
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))

        SignupLink(onSignupClick = onNavigateToSignUp)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LoginForm(
    username: String,
    onUsernameChange: (String) -> Unit,
    isValidUsername: Boolean,
    password: String,
    onPasswordChange: (String) -> Unit,
    isValidPassword: Boolean,
    onLoginClick: () -> Unit,
    onSocialLoginClick: () -> Unit
) {
    Column(
        Modifier.padding(
            start = 16.dp,
            top = 24.dp,
            end = 16.dp,
            bottom = 16.dp
        )
    ) {

        UsernameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colors.background),
            value = username,
            onValueChange = onUsernameChange,
            isError = !isValidUsername,
            imeAction = ImeAction.Next
        )

        val keyboardController = LocalSoftwareKeyboardController.current
        PasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colors.background),
            value = password,
            label = stringResource(R.string.password),
            onValueChange = onPasswordChange,
            isError = !isValidPassword,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                keyboardController?.hide()
            }
        )

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(60.dp)
                .testTag("LoginBtn"),
            shape = RoundedCornerShape(8),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.buttonColor)
        ) {
            BooksriverSubtitle(fontSize = 16.sp, color = Color.White, text = stringResource(R.string.login))
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(Modifier.width(15.dp), color = MaterialTheme.colors.textCaptionColor)
            BooksriverCaption(text = stringResource(R.string.or), modifier = Modifier.padding(horizontal = PaddingSmall))
            Divider(Modifier.width(15.dp), color = MaterialTheme.colors.textCaptionColor)
        }
        Button(
            onClick = onSocialLoginClick,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(8),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFDB4437))
        ) {
            BooksriverSubtitle(fontSize = 16.sp, color = Color.White, text = stringResource(R.string.google_login))
        }
    }
}

@Composable
private fun SignupLink(onSignupClick: () -> Unit) {
    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingLarge),
        onClick = onSignupClick
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.textCaptionColor)) {
                    append(stringResource(R.string.no_account_question))
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.titleColor,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(" " + stringResource(R.string.sign_up_exclamation))
                }
            },
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Preview
@Composable
fun PreviewLoginContent() = BooksriverTheme {
    LoginContent(
        isLoading = false,
        username = "ghibo",
        onUsernameChange = {},
        onPasswordChange = {},
        password = "password",
        onNavigateToSignUp = {},
        onLoginClick = {},
        isValidUsername = false,
        isValidPassword = false,
        error = null,
        showFailureDialog = false,
        onDismissFailureDialog = {},
        onSocialLoginClick = {}
    )
}
