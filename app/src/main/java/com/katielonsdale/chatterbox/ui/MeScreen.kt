package com.katielonsdale.chatterbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.api.data.UserUiState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.katielonsdale.chatterbox.R


@Composable
fun MeScreen(
    currentUser: UserUiState,
    onClickEdit: () -> Unit,
    onClickLogOut: () -> Unit,
){
    val displayedUser = remember {mutableStateOf(currentUser)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        //todo: placeholder for future profile picture
        CircleWithText(
            text = displayedUser.value.displayName
        )
        Spacer(modifier = Modifier.padding(10.dp))
        //todo: enable user to edit profile
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .padding(
//                    top = 20.dp,
//                    bottom = 20.dp
//                )
//                .fillMaxWidth()
//        ){
//            Text(
//                text = "Edit",
//                fontSize = 30.sp,
//            )
//            IconButton(
//                onClick = { onClickEdit }
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.edit_icon_24dp),
//                    contentDescription = "edit profile icon",
//                    modifier = Modifier.size(35.dp),
//                    tint = Color.DarkGray
//                )
//            }
//        }
        Text(
            text = displayedUser.value.email,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = "Notification Frequency: " + displayedUser.value.notificationFrequency,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        //todo: enable user to change password
//        TextButton(
//            onClick = { /*TODO*/ },
//            colors = ButtonDefaults.textButtonColors(
//                containerColor = Color.LightGray,
//                contentColor = Color.DarkGray
//            )
//        ) {
//            Text("Change Password")
//        }

        ElevatedButton(
            onClick = onClickLogOut,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.DarkGray,
            ),
        ) {
            Text("Log Out")
        }
    }
}

@Composable
fun CircleWithText(
    text: String,
    size: Dp = 250.dp,
    backgroundColor: Color = Color.LightGray,
    textColor: Color = Color.DarkGray
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}


@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewMeScreen() {
    val currentUser = UserUiState(
        id = "1",
        email = "example@gmail.com",
        displayName = "currentUser123",
        notificationFrequency = "live",
    )
    Surface() {
        MeScreen(
            currentUser = currentUser,
            onClickEdit = {},
            onClickLogOut = {}
        )
    }
}
