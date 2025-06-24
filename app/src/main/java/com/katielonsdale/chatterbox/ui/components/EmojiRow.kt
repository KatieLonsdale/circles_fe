package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme

// currently a mockup, real functionality added later
@Composable
fun EmojiRow(
    startPadding: Dp,
){
    Row(
        modifier = Modifier
            .padding(
                start = startPadding,
                top = 5.dp,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.star_emoji
            ),
            contentDescription = "star emoji",
            colorFilter = ColorFilter.tint(Yellow)
        )

        Text(
            text = "5",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 15.sp
            )
        )

        Spacer(Modifier.width(2.dp))

        Image(
            painter = painterResource(
                id = R.drawable.heart_emoji
            ),
            contentDescription = "heart emoji",
            colorFilter = ColorFilter.tint(Red)
        )

        Text(
            text = "3",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 15.sp
            )
        )

        Spacer(Modifier.width(2.dp))

        Image(
            painter = painterResource(
                id = R.drawable.smile_emoji
            ),
            contentDescription = "smile emoji",
            colorFilter = ColorFilter.tint(Yellow)
        )

        Text(
            text = "7",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 15.sp
            )
        )

    }
}

@Preview
@Composable
fun PreviewEmojiRow(){
    ChatterBoxTheme {
        EmojiRow(
            startPadding = 0.dp,
        )
    }
}