package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Comment
import com.katielonsdale.chatterbox.api.data.CommentViewModel
import androidx.compose.ui.text.AnnotatedString


// for home and circle newsfeeds, does not allow replies
@Composable
fun NewsfeedCommentCard(
    comment: Comment,
    ) {
    Column(
        modifier = Modifier
            .padding(
                start = 5.dp,
                end = 5.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

                Row(
                    modifier = Modifier
                        .weight(0.9f)
//                        .padding(all = 10.dp),
                ) {
                    // Author name
                    Text(
                        text = comment.attributes.authorDisplayName,
                        color = Color.DarkGray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    // Comment text
                    SelectionContainer {
                        Text(
                            //reset: AnnotatedString breaks preview
//                            text = AnnotatedString(comment.attributes.commentText),
                            text = comment.attributes.commentText,
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                        )
                    }
                }
        }

        Spacer(modifier = Modifier.height(10.dp))
        // Reply icon at the end
        val commentReplies = comment.attributes.replies?.data
        if (!commentReplies.isNullOrEmpty()) {
            val sortedReplies = commentReplies.sortedBy { it.attributes.createdAt }
            for (reply in sortedReplies) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_reply),
                        contentDescription = "Reply",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .padding(start = 15.dp),
                    )
                        Text(
                            text = reply.attributes.authorDisplayName,
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            // reset: AnnotatedString breaks preview
//                                text = AnnotatedString(reply.attributes.commentText),
                            text = reply.attributes.commentText,
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(end = 10.dp)
//                                .align(Alignment.CenterVertically)
                        )
//                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewNewsfeedCommentCard() {
    val posts = SampleData.returnSamplePosts()
    val post = posts[0]
    val comments = post.attributes.comments.data
    NewsfeedCommentCard(
        comment = comments[0],
    )
}