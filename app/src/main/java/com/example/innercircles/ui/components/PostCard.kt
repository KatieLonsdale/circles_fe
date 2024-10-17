package com.example.innercircles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.innercircles.R
import com.example.innercircles.SampleData
import com.example.innercircles.api.data.Post
import com.example.innercircles.ui.home.CommentCard

@Composable
fun PostCard(
    post: Post,
    onClickDisplayPost: (Post) -> Unit,
    displayCircleName: Boolean = true
) {
    val medias = post.attributes.contents.data
    var hasMedia = true;
    if (medias.isEmpty()) { hasMedia = false }
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxHeight()
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
        val textSize: Int = if (!hasMedia) {
            25
        } else {
            15
        }

        Row(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Text(
                text = post.attributes.authorDisplayName,
                color = Color.DarkGray,
                fontSize = textSize.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = post.attributes.caption,
                color = Color.DarkGray,
                fontSize = textSize.sp,
            )
        }

        val comments = post.attributes.comments.data
        if (comments.isNotEmpty()) {
            Column(
                Modifier
                    .padding(start = 20.dp, top = 5.dp, bottom = 5.dp)
            ) {
                comments.take(2).forEach { comment ->
                    CommentCard(comment)
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
                    fontSize = 15.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PostCardPreview(){
    val posts = SampleData.returnSamplePosts()
    PostCard(
        posts[0],
        onClickDisplayPost = {},
        true
    )
}