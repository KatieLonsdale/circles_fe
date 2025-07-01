package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.R


@Composable
fun SelectChatters(
    chatters: List<Circle>,
    selectedChatterIds: MutableList<String>
){
    Surface(
        shape = MaterialTheme.shapes.small,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background.copy(
                    alpha = 0.7F
                ))
        ) {
            Text(
                text = "Post to Chatters:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(
                    5.dp
                )
            )
            LazyColumn(
                modifier = Modifier.padding(
                    start = 5.dp,
                    end = 5.dp
                )
            ) {
                items(chatters) { chatter ->
                    val selected = selectedChatterIds.contains(chatter.id)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.7F
                                ) else Color.Transparent
                            )
                            .clickable {
                                onGroupToggle(
                                    chatterId = chatter.id,
                                    selectedChatterIds = selectedChatterIds,
                                )
                            }
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 5.dp,
                            )
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://images.unsplash.com/photo-1454789548928-9efd52dc4031?q=80&w=1160&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.my_chatters_nav),
                            error = painterResource(R.drawable.ic_image_error_24dp),
                            contentDescription = "Chatter image",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = chatter.attributes.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    }
}

fun onGroupToggle(
    chatterId: String,
    selectedChatterIds: MutableList<String>
) {
    if (selectedChatterIds.contains(chatterId)) {
        selectedChatterIds.remove(chatterId)
    } else {
        selectedChatterIds.add(chatterId)
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSelectChatters(){
    val chatters = SampleData.returnSampleChatters
    val selectedChatterIds = remember {mutableStateListOf<String>()}
    ChatterBoxTheme{
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondary.copy(
                        alpha = (0.5F)
                    )
                )
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            SelectChatters(
                chatters = chatters,
                selectedChatterIds = selectedChatterIds
            )
        }
    }
}