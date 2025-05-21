package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.katielonsdale.chatterbox.R

// Box for back arrow
@Composable
fun BackButton(
    onClickBack: () -> Unit
){
    Box() {
        IconButton(
            onClick = {
                onClickBack()
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White
            ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back), // Use your back arrow drawable
                contentDescription = "Back",
                modifier = Modifier.align(Alignment.TopStart)
                    .minimumInteractiveComponentSize(),
            )
        }
    }
}