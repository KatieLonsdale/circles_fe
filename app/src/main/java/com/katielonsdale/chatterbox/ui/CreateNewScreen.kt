package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateNewScreen(
    onClickNewPost: () -> Unit,
    onClickNewCircle: () -> Unit,
    onClickNewFriend: () -> Unit,
){
    val options: Array<Pair<String, () -> Unit>> = arrayOf(
        "Post" to onClickNewPost,
        "Chatter" to onClickNewCircle,
        "Friend" to onClickNewFriend,
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = "Create New...",
            color = Color.DarkGray,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = 10.dp,
                top = 10.dp,
            )
        )
        options.forEach { option ->
            Row(
                modifier = Modifier.clickable { option.second() }
                    .fillMaxWidth()
                    .padding(
                        start = 25.dp,
                        top = 5.dp,
                    )
            ) {
                Text(
                    text = option.first,
                    color = Color.DarkGray,
                    fontSize = 25.sp,
                    modifier = Modifier.align(Alignment.Bottom)
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCreateNewScreen(){
    MaterialTheme {
        CreateNewScreen(
            onClickNewPost = {},
            onClickNewCircle = {},
            onClickNewFriend = {},
        )
    }
}