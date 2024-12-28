package com.katielonsdale.chatterbox.ui.notifications

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NotificationsScreen(
    logOutUser: () -> Unit
){
    ElevatedButton(
        onClick = logOutUser,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray, // Background color
            contentColor = Color.DarkGray,  // Text color
        ),
        modifier = Modifier
//            .align(Alignment.BottomEnd) // Align to the bottom-right
            .padding(16.dp) // Padding to prevent the button from touching the screen edge
    ) {
        Text("Log Out")
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview(){
    NotificationsScreen (
        logOutUser = {}
    )
}