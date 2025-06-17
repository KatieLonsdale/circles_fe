package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme

// Box for back arrow
@Composable
fun BackButton(
    onClickBack: () -> Unit
){
    IconButton(
        onClick = {
            onClickBack()
        },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .minimumInteractiveComponentSize(),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
@Preview(apiLevel = 34, showBackground = true)
fun BackButtonPreview(){
    ChatterBoxTheme {
        BackButton(onClickBack = {})
    }
}