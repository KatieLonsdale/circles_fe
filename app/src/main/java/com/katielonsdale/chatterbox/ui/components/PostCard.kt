package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Post
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.util.Locale

val TAG = "Post Card"


// used for Newsfeed and Circle Newsfeed, only displays 2 comments
@Composable
fun PostCard(
    post: Post,
    onClickDisplayPost: (Post) -> Unit,
    displayCircleName: Boolean = true
) {
    val medias = post.attributes.contents.data
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClickDisplayPost(post) }
            .shadow(
                8.dp,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .background(
                MaterialTheme.colorScheme.surface
            ),
        verticalArrangement = Arrangement.Center,

    ) {
        for (media in medias) {
            val imageUrl = media.attributes.imageUrl
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_image_error_24dp),
                contentDescription = stringResource(R.string.description),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //todo: users can add profile pictures
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data("")
//                    .crossfade(true)
//                    .build(),
//                placeholder = painterResource(id = R.drawable.me_nav),
//                error = painterResource(R.drawable.ic_image_error_24dp),
//                contentDescription = stringResource(R.string.description),
//                modifier = Modifier
//                    .clip(shape = CircleShape)
//                    .height(30.dp)
//                    .width(30.dp)
//            )
            Image(
                painter = painterResource(R.drawable.me_nav),
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .height(30.dp)
                    .width(30.dp),
                contentDescription = "place holder for profile picture"
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = post.attributes.authorDisplayName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alignByBaseline()
            )

            Spacer(Modifier.width(5.dp))
            Text(
                text = formatTimeStamp(post.attributes.updatedAt),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 15.sp,
                ),
                modifier = Modifier.alignByBaseline(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


        Row(
            modifier = Modifier.padding(start = 36.dp)
        ) {
            Text(
                text = post.attributes.caption,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
        }

        val comments = post.attributes.comments.data
        if (comments.isNotEmpty()) {
            Column(
                Modifier
                    .padding(start = 15.dp)
            ) {
                comments.take(2).forEach { comment ->
                    NewsfeedCommentCard(
                        comment = comment,

                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (displayCircleName) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = post.attributes.circleName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "See conversation",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
                Icon(
                    painter = painterResource(
                        id = R.drawable.arrow_right
                    ),
                    contentDescription = "arrow right"
                )
            }

        }
        Spacer(Modifier.height(5.dp))
    }
}

fun formatTimeStamp(originalTimestamp: String): String {
    return try {
        val deviceZone = ZoneId.systemDefault()
        val zonedDateTime = ZonedDateTime.parse(originalTimestamp)
            .withZoneSameInstant(deviceZone)

        val now = ZonedDateTime.now(deviceZone)
        val date = zonedDateTime.toLocalDate()
        val today = now.toLocalDate()
        val yesterday = today.minusDays(1)

        val timePart = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))

        return when (date) {
            today -> "Today $timePart"
            yesterday -> "Yesterday $timePart"
            else -> {
                val dateFormatter = DateTimeFormatter.ofPattern("M/d/yy", Locale.getDefault())
                "${zonedDateTime.format(dateFormatter)} $timePart"
            }
        }

    } catch (e: Exception) {
        "Invalid date"
    }
}


@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PostCardPreview(){
    val posts = SampleData.returnSamplePosts()
    val post = posts.first()
    ChatterBoxTheme {
        PostCard(
            post,
            onClickDisplayPost = {},
            true
        )
    }
}