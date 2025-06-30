package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import com.katielonsdale.chatterbox.R
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.ui.components.NewOptionIcon



@Composable
fun CreateNewScreen(
    onClickNewPost: () -> Unit,
    onClickNewChatter: () -> Unit,
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.padding(
                10.dp
            )
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
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Create New...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    NewOptionIcon(
                        action = onClickNewPost,
                        icon = R.drawable.new_post,
                        label = "Post"
                    )
                    Spacer(Modifier.width(25.dp))
                    NewOptionIcon(
                        action = onClickNewChatter,
                        icon = R.drawable.new_chatter,
                        label = "Chatter"
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCreateNewScreen(){
    ChatterBoxTheme {
        CreateNewScreen(
            onClickNewPost = {},
            onClickNewChatter = {},
        )
    }
}