package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Comment
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.CommentUiState
import com.katielonsdale.chatterbox.api.data.CommentViewModel
import com.katielonsdale.chatterbox.ui.CommentInput

var replyInputVisible = false
@Composable
fun CommentCard(
    comment: Comment,
    circleId: String? = "",
    commentViewModel: CommentViewModel = viewModel(),
    ) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Combined author name and comment text
            Surface(
                shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
                color = Color.LightGray,
            ) {
                Row(
                    modifier = Modifier
                        .weight(0.9f)
                        .padding(all = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
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
                            text = comment.attributes.commentText,
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.1f)
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                ReplyIconButton()
            }

        }

        Spacer(modifier = Modifier.height(10.dp))
        // Reply icon at the end
        val commentReplies = comment.attributes.replies
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
                            .padding(start = 25.dp),
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
                        color = Color.LightGray,
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                top = 10.dp,
                                bottom = 10.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = reply.attributes.authorDisplayName,
                                color = Color.DarkGray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 10.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = reply.attributes.commentText,
                                color = Color.DarkGray,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(0.1f)
                            .padding(end = 10.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        ReplyIconButton()
                    }
                }
                val secondCommentReplies = reply.attributes.replies
                if (!secondCommentReplies.isNullOrEmpty()) {
                    val sortedSubReplies = secondCommentReplies.sortedBy { it.attributes.createdAt }
                    for (subReply in sortedSubReplies) {
                        Spacer(modifier = Modifier.height(5.dp))
                        FinalCommentCard(reply = subReply)
                    }
                    //reset: for editing in preview
//                    replyInputVisible = true
                    if (replyInputVisible){
                        val commentUiState by commentViewModel.uiState.collectAsState()
                        CommentInput(
                            value = commentUiState.commentText,
                            commentViewModel = commentViewModel,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun ReplyIconButton() {
    IconButton(
        onClick = { replyInputVisible = true },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_reply),
            contentDescription = "Reply",
            tint = Color.DarkGray,
            modifier = Modifier
                .minimumInteractiveComponentSize()
        )
    }
}

@Composable
fun CommentInput(
    value: String,
    commentViewModel: CommentViewModel,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_reply),
            contentDescription = "Reply",
            tint = Color.DarkGray,
            modifier = Modifier
                .minimumInteractiveComponentSize()
        )

        TextField(
            value = value,
            onValueChange = { newText ->   // Update the state with the new text
                commentViewModel.setCommentText(newText)
            },
            //todo: "reply to {username}"
            placeholder = { Text("Reply to comment...") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
                replyInputVisible = false
            }),
            textStyle = TextStyle(color = Color.DarkGray)
        )
    }
}

@Composable
fun FinalCommentCard(
    reply: Comment
) {
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
                .padding(start = 50.dp),
        )

        Surface(
            shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
            color = Color.LightGray,
        ) {
            Row(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = reply.attributes.authorDisplayName,
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = reply.attributes.commentText,
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
            }
        }
    }
}

@Composable
fun ReplyButton(){

}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCommentCard() {
    val posts = SampleData.returnSamplePosts()
    val post = posts[0]
    val comments = post.attributes.comments.data
    CommentCard(
        comment = comments[0],
        post.attributes.circleId
    )
}