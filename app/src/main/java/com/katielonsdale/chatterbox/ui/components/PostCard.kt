package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Post
import com.katielonsdale.chatterbox.ui.components.NewsfeedCommentCard
import sh.calvin.autolinktext.rememberAutoLinkText
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


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
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onClickDisplayPost(post) }
            .shadow(
                8.dp,
                shape = RoundedCornerShape(16.dp)
            )  // Apply shadow with rounded corners
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)  // Set background color (e.g., white)
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
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        val fontSize = 25.sp

        Row(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Text(
                text = post.attributes.authorDisplayName,
                color = Color.DarkGray,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(5.dp))


            Text(
                text = formatTimeStamp(post.attributes.updatedAt),
                color = Color.DarkGray,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp)) // Small gap between header and comment

        Row(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Text(
//                reset: annotatedstring breaks previews
                text = AnnotatedString.rememberAutoLinkText(text = post.attributes.caption),
//                text = post.attributes.caption,
                color = Color.DarkGray,
                fontSize = fontSize,
            )
        }

        val comments = post.attributes.comments.data
        if (comments.isNotEmpty()) {
            Column(
                Modifier
                    .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
            ) {
                comments.take(2).forEach { comment ->
                    NewsfeedCommentCard(
                        comment = comment,
                        circleId = post.attributes.circleId
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (displayCircleName) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = post.attributes.circleName,
                    color = Color.DarkGray,
                    fontSize = fontSize,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

fun formatTimeStamp(originalTimestamp: String) : String{
    try {
        // Parse the ISO-8601 timestamp and format it
        val zonedDateTime = ZonedDateTime.parse(originalTimestamp)
        return zonedDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"))
    } catch (e: Exception) {
        return "Invalid date" // Fallback if parsing fails
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PostCardPreview(){
    val posts = SampleData.returnSamplePosts()
    PostCard(
        posts[0],
        onClickDisplayPost = {},
        true
    )
}