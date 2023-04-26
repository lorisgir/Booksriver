package com.example.booksriver.ui.components.userlibrary_dialog

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.booksriver.R
import com.example.booksriver.component.BooksriverTextField
import com.example.booksriver.component.ConfirmDismissDialog
import com.example.booksriver.data.model.UserLibrary
import com.example.booksriver.util.collectState

@Composable
fun UserLibraryDialog(
    viewModel: UserLibraryDialogViewModel,
    onDismissed: () -> Unit,
    onConfirmed: (UserLibrary) -> Unit,
    onConfirmedError: (String) -> Unit,
    isCreate: Boolean = true,
    libraryId: Int? = null,
    libraryName: String? = null
) {
    val state by viewModel.collectState()


    if (libraryName != null && libraryId != null) {
        LaunchedEffect(Unit){
            viewModel.setLibrary(libraryId, libraryName)
        }
    }

    ConfirmDismissDialog(
        title = stringResource(id = if (isCreate) R.string.create_new_lib else R.string.edit_name),
        confirmButtonText = stringResource(id = if (isCreate) R.string.create else R.string.edit),
        onDismissed = onDismissed,
        onConfirmed = {
            viewModel.saveLibrary(onConfirmed, onConfirmedError, isCreate)
        }
    ) {
        BooksriverTextField(
            value = state.newLibraryName,
            label = stringResource(R.string.library_name),
            isError = state.isValidLibName == false,
            onValueChange = viewModel::setLibraryName,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                viewModel.saveLibrary(onConfirmed, onConfirmedError, isCreate)
            }
        )
    }
}
