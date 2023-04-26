package com.example.booksriver.component;

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksriver.R
import com.example.booksriver.theme.textPrimaryColor


@Preview
@Composable
fun UsernameTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    helperText: String = "",
    isError: Boolean = false,
    onValueChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    BooksriverTextField(
        value = value,
        label = stringResource(R.string.username),
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = { Icon(Icons.Outlined.Person, stringResource(R.string.user)) },
        isError = isError,
        helperText = helperText,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Preview
@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    helperText: String = "",
    isError: Boolean = false,
    onValueChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    BooksriverTextField(
        value = value,
        label = stringResource(R.string.email),
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = { Icon(Icons.Outlined.Email, stringResource(R.string.email)) },
        isError = isError,
        helperText = helperText,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Preview
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.password),
    value: String = "",
    helperText: String = "",
    isError: Boolean = false,
    onValueChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    BooksriverTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = { Icon(Icons.Outlined.Password, label) },
        visualTransformation = PasswordVisualTransformation(),
        isError = isError,
        helperText = helperText,
        imeAction = imeAction,
        onAction = onAction
    )
}


@Preview
@Composable
fun BooksriverTextField(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    onValueChange: (String) -> Unit = {},
    fontSize: TextUnit = 16.sp,
    color: Color = MaterialTheme.colors.onBackground,
    leadingIcon: @Composable() (() -> Unit)? = null,
    isError: Boolean = false,
    helperText: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    trailingIcon: @Composable () -> Unit = {}
) {
    Surface(modifier) {
        Column {
            OutlinedTextField(
                value = value,
                label = { Text(text = label) },
                modifier = textModifier
                    .fillMaxWidth()
                    .testTag(label),
                onValueChange = onValueChange,
                leadingIcon = leadingIcon,
                textStyle = TextStyle(color, fontSize = fontSize),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.textPrimaryColor,
                    unfocusedBorderColor = MaterialTheme.colors.textPrimaryColor,
                    focusedLabelColor = MaterialTheme.colors.textPrimaryColor,
                    cursorColor = MaterialTheme.colors.textPrimaryColor,
                    leadingIconColor =  MaterialTheme.colors.textPrimaryColor,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                isError = isError,
                visualTransformation = visualTransformation,
                shape = RoundedCornerShape(8.dp),
                keyboardActions = onAction,
                maxLines = maxLines,
                trailingIcon = trailingIcon
            )
            if (helperText.isNotEmpty()) {
                Spacer(modifier = Modifier.padding(2.dp))
                BooksriverCaption(text = helperText, fontSize = 12.sp)
            }
        }
    }
}


@Preview
@Composable
fun BasicTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    onTextChange: (String) -> Unit = {},
    maxLines: Int = Int.MAX_VALUE,
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onTextChange,
        textStyle = textStyle.copy(color = MaterialTheme.colors.onPrimary),
        maxLines = maxLines,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        decorationBox = { inlineTextField ->
            AnimatedVisibility(visible = value.isBlank()) {
                Text(
                    text = label,
                    style = textStyle
                )
            }
            inlineTextField()
        }
    )
}

