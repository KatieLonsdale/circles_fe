package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.ui.components.BackButton

@Composable
fun CreateNewScreen(
    onClickBack: () -> Unit,
    onClickNewPost: () -> Unit,
    onClickNewCircle: () -> Unit,
    onClickNewFriend: () -> Unit,
){
    val options: Array<Pair<String, () -> Unit>> = arrayOf(
        "Post" to onClickNewPost,
        "Circle" to onClickNewCircle,
        "Friend" to onClickNewFriend,
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        BackButton(onClickBack = onClickBack)
        Text(
            text = "Select New...",
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
                        start = 15.dp,
                        top = 5.dp,
                    )
            ) {
                Text(
                    text = option.first,
                    color = Color.DarkGray,
                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
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
            onClickBack = {},
            onClickNewPost = {},
            onClickNewCircle = {},
            onClickNewFriend = {},
        )
    }
}