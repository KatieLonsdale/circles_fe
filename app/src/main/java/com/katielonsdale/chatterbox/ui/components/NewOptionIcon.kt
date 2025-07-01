package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme

@Composable
fun NewOptionIcon(
    action: () -> Unit = {},
    icon: Int,
    label: String,
){
    val canvasColor = MaterialTheme.colorScheme.surfaceVariant.copy(
        alpha = 0.5F
    )
    Column(
        modifier = Modifier
            .clickable {action()}
            .size(150.dp)
            .drawBehind {
                drawCircle (
                    color = canvasColor,
                    radius = 200F
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "New post",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewOptionIcon(){
    ChatterBoxTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(
                            alpha = (0.5F)
                        )
                    )
            ) {
                NewOptionIcon(
                    icon = R.drawable.new_post,
                    label = "Post"
                )
            }
        }
    }
}