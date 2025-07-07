package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.api.data.Friend
import com.katielonsdale.chatterbox.api.data.states.NewChatterUiState
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.ui.components.NewOptionIcon
import com.katielonsdale.chatterbox.ui.components.TextFieldOnSurface
import com.katielonsdale.chatterbox.ui.components.SelectFriends
import com.katielonsdale.chatterbox.api.data.source.ChatterDataSource.createChatter
import com.katielonsdale.chatterbox.api.data.viewModels.NewChatterViewModel

val TAG = "NewChatterScreen"

@Composable
fun NewChatterScreen(
    currentUserFriends: List<Friend>,
    onClickCreate: () -> Unit,
    newChatterUiState: NewChatterUiState,
    onEvent: (NewChatterViewModel.MyEvent) -> Unit
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    // Remove focus and dismiss keyboard when tapping outside the TextField
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(
                            alpha = (0.5F)
                        )
                    )
                    .padding(
                        top = 20.dp,
                        bottom = 20.dp,
                        start = 10.dp,
                        end = 10.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    NewOptionIcon(
                        icon = R.drawable.new_chatter,
                        label = "Chatter"
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    TextFieldOnSurface(
                        value = newChatterUiState.name,
                        onValueChange = { name ->
                            onEvent(NewChatterViewModel.MyEvent.SetName(name))
                        },
                        label = "Chatter Name",
                        maxLines = 2,
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    TextFieldOnSurface(
                        value = newChatterUiState.description,
                        onValueChange = { description ->
                            onEvent(NewChatterViewModel.MyEvent.SetDescription(description))
                        },
                        label = "Description of your chatter",
                        maxLines = 5,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            // add image to new chatter //
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.5F
                            ),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Text(
                            text = "Add Image",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                        ),
                ){
//                    if (userFriends.isNotEmpty()) {
//                        SelectFriends(
//                            friends = userFriends,
//                            newChatterUiState = newChatterUiState,
//                            onEvent = onEvent,
//                        )
//                    } else {
                        Text(
                            text = "Please add friends before creating a Chatter.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
//                    }
                }

                Spacer(Modifier.height(20.dp))

                if (currentUserFriends.isNotEmpty() && newChatterUiState.memberIds.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                createChatter(
                                    chatterName = newChatterUiState.name,
                                    chatterDescription = newChatterUiState.description,
                                )
                                onClickCreate()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                    alpha = 0.5F
                                ),
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                        ) {
                            Text(
                                text = "Create",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NewChatterScreenPreview() {
    ChatterBoxTheme {
        NewChatterScreen(
            currentUserFriends = emptyList<Friend>(),
            onClickCreate = {},
            newChatterUiState = NewChatterUiState(),
            onEvent = {}
        )
    }
}