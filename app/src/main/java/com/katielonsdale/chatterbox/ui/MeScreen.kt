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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.api.data.UserUiState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.Dp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import androidx.compose.foundation.layout.height


@Composable
fun MeScreen(
    currentUser: UserUiState,
    onClickEdit: () -> Unit,
    onClickLogOut: () -> Unit,
){
//    Row(
//        verticalAlignment = Alignment.Top,
//        horizontalArrangement = Arrangement.End,
//        modifier = Modifier
//            .padding(
//                top = 20.dp,
//                bottom = 20.dp
//            )
//            .fillMaxWidth()
//    ){
//        IconButton(
//            onClick = { onClickEdit }
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.edit_icon_24dp),
//                contentDescription = "edit profile icon",
//                modifier = Modifier.size(35.dp),
//                tint = MaterialTheme.colorScheme.primary
//            )
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row(){
            Icon(
                painter = painterResource(id = R.drawable.me_nav),
                contentDescription = "user icon",
                modifier = Modifier.size(300.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = currentUser.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    start = 50.dp,
                ),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Email: ",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = Bold,
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = currentUser.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alignByBaseline()
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 50.dp,
                ),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Notification Frequency: ",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = Bold,
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = currentUser.notificationFrequency,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alignByBaseline()
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
//        ElevatedButton(
//            onClick = { /*TODO*/ },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.background,
//            ),
//        ) {
//            Text(
//                text = "Change Password",
//                style = MaterialTheme.typography.labelSmall
//            )
//        }
//        Spacer(modifier = Modifier.height(10.dp))

        ElevatedButton(
            onClick = onClickLogOut,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Text(
                text = "Log Out",
                style = MaterialTheme.typography.labelSmall
            )
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
    ChatterBoxTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
            ){
                MeScreen(
                    currentUser = currentUser,
                    onClickEdit = {},
                    onClickLogOut = {}
                )
        }
    }
}
