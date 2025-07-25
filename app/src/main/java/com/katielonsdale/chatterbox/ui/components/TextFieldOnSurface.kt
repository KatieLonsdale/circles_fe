package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme

@Composable
fun TextFieldOnSurface(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    hidden: Boolean = false,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (!hidden) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                )
            },
            textStyle = MaterialTheme.typography.labelSmall,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = modifier,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
        )
    } else {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                )
            },
            textStyle = MaterialTheme.typography.labelSmall,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = modifier,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            visualTransformation = PasswordVisualTransformation(),
        )
    }
}

@Preview(apiLevel = 34)
@Composable
fun PreviewTextFieldOnSurface(){
    ChatterBoxTheme {
        TextFieldOnSurface(
            value = "",
            onValueChange = {},
            label = "label",
        )
    }
}