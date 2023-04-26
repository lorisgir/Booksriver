package com.example.booksriver.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.booksriver.theme.*
import com.example.booksriver.util.collectState

@Composable
fun SignUpScreen(
    viewModel: SignupViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val state by viewModel.collectState()

    SignUpContent(
        isLoading = state.isLoading,
        username = state.username,
        email = state.email,
        password = state.password,
        confirmPassword = state.confirmPassword,
        isValidUsername = state.isValidUsername ?: true,
        isValidEmail = state.isValidEmail ?: true,
        isValidPassword = state.isValidPassword ?: true,
        isValidConfirmPassword = state.isValidConfirmPassword ?: true,
        onUsernameChange = viewModel::setUsername,
        onEmailChange = viewModel::setEmail,
        onPasswordChange = viewModel::setPassword,
        onConfirmPasswordChanged = viewModel::setConfirmPassword,
        onSignUpClick = viewModel::register,
        onNavigateToLogin = onNavigateToLogin,
        error = state.error,
        showFailureDialog = state.showFailureDialog,
        onDismissFailureDialog = viewModel::dismissFailureDialog
    )

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onNavigateToMain()
        }
    }
}

@Composable
fun SignUpContent(
    isLoading: Boolean,
    username: String,
    email: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    password: String,
    confirmPassword: String,
    onConfirmPasswordChanged: (String) -> Unit,
    isValidConfirmPassword: Boolean,
    onNavigateToLogin: () -> Unit,
    onSignUpClick: () -> Unit,
    isValidUsername: Boolean,
    isValidEmail: Boolean,
    isValidPassword: Boolean,
    error: String?,
    showFailureDialog: Boolean,
    onDismissFailureDialog: () -> Unit
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
            .verticalScroll(rememberScrollState())
    ) {
        BooksriverTitle1(
            text = stringResource(R.string.create_account_title),
            fontSize = 34.sp,
            maxLines = 2,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 16.dp)
        )

        SignUpForm(
            username = username,
            onUsernameChange = onUsernameChange,
            isValidUsername = isValidUsername,
            email = email,
            onEmailChange = onEmailChange,
            isValidEmail = isValidEmail,
            password = password,
            onPasswordChange = onPasswordChange,
            isValidPassword = isValidPassword,
            confirmPassword = confirmPassword,
            onConfirmPasswordChanged = onConfirmPasswordChanged,
            isValidConfirmPassword = isValidConfirmPassword,
            onSignUpClick = onSignUpClick
        )

        Spacer(modifier = Modifier.weight(1f))

        LoginLink(onLoginClick = onNavigateToLogin)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SignUpForm(
    username: String,
    onUsernameChange: (String) -> Unit,
    isValidUsername: Boolean,
    email: String,
    onEmailChange: (String) -> Unit,
    isValidEmail: Boolean,
    password: String,
    onPasswordChange: (String) -> Unit,
    isValidPassword: Boolean,
    confirmPassword: String,
    onConfirmPasswordChanged: (String) -> Unit,
    isValidConfirmPassword: Boolean,
    onSignUpClick: () -> Unit
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
            helperText = stringResource(R.string.message_field_username_invalid),
            onValueChange = onUsernameChange,
            isError = !isValidUsername,
            imeAction = ImeAction.Next
        )

        EmailTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colors.background),
            value = email,
            helperText = stringResource(R.string.message_field_blank),
            onValueChange = onEmailChange,
            isError = !isValidEmail,
            imeAction = ImeAction.Next
        )

        PasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colors.background),
            value = password,
            helperText = stringResource(R.string.message_field_password_invalid),
            onValueChange = onPasswordChange,
            isError = !isValidPassword,
            imeAction = ImeAction.Next
        )

        val keyboardController = LocalSoftwareKeyboardController.current
        PasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colors.background),
            value = confirmPassword,
            label = stringResource(R.string.confirm_password),
            helperText = stringResource(R.string.message_field_password_invalid),
            onValueChange = onConfirmPasswordChanged,
            isError = !isValidConfirmPassword,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                keyboardController?.hide()
            }
        )

        Button(
            onClick = onSignUpClick,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.buttonColor)
        ) {
            BooksriverSubtitle(fontSize = 16.sp, color = Color.White, text = stringResource(R.string.create_account))
        }
    }
}

@Composable
private fun LoginLink(onLoginClick: () -> Unit) {
    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingLarge),
        onClick = onLoginClick
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.textCaptionColor)) {
                    append(stringResource(R.string.already_have_account_question))
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.titleColor,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(" " + stringResource(R.string.login_exclamation))
                }
            },
            style = typography.subtitle1
        )
    }
}

@Preview
@Composable
fun PreviewSignupContent() = BooksriverTheme {
    SignUpContent(
        isLoading = false,
        username = "ghibo",
        email = "mail@gmail.com",
        onUsernameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        password = "password",
        confirmPassword = "password",
        onConfirmPasswordChanged = {},
        isValidConfirmPassword = false,
        onNavigateToLogin = {},
        onSignUpClick = {},
        isValidUsername = false,
        isValidEmail = false,
        isValidPassword = false,
        error = null,
        showFailureDialog = false,
        onDismissFailureDialog = {}
    )
}
