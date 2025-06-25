package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.katielonsdale.chatterbox.api.data.CommentViewModel
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.utils.CommentCreator.createComment
import sh.calvin.autolinktext.rememberAutoLinkText


@Composable
fun CommentAndRepliesCard(
    comment: Comment,
    postId: String,
    circleId: String,
    addCommentToPost: (Comment) -> Unit,
    commentViewModel: CommentViewModel = viewModel(),
    replyVisibilityId: MutableState<String> = remember { mutableStateOf("") },
) {
    val spacer = Spacer(modifier = Modifier.height(5.dp))

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CommentCard(
            comment = comment,
            onClick = {
                val id = "comment ${comment.id}"
                replyVisibilityId.value = if (replyVisibilityId.value == id) "" else id
            },

        )

        spacer

        val commentReplies = comment.attributes.replies?.data
        if (!commentReplies.isNullOrEmpty()) {
            val sortedReplies = commentReplies.sortedBy { it.attributes.createdAt }
            for (reply in sortedReplies) {
                CommentCard(
                    comment = reply,
                    onClick = {
                        val id = "reply ${reply.id}"
                        replyVisibilityId.value =
                            if (replyVisibilityId.value == id) "" else id
                    },
                    startPadding = 25.dp
                )

                spacer

                val secondCommentReplies = reply.attributes.replies?.data
                if (!secondCommentReplies.isNullOrEmpty()) {
                    val sortedSubReplies = secondCommentReplies.sortedBy { it.attributes.createdAt }
                    for (subReply in sortedSubReplies) {
                        CommentCard(
                            comment = subReply,
                            onClick = {
                                val id = "reply ${reply.attributes.parentCommentId}"
                                replyVisibilityId.value = if (replyVisibilityId.value == id) "" else id
                            },
                            startPadding = 50.dp
                        )
                        spacer
                    }
                }
                spacer
                //reset: for editing in preview
//              if (true){
                if (replyVisibilityId.value == "reply ${reply.id}") {
                    val commentUiState by commentViewModel.uiState.collectAsState()
                    CommentInput(
                        value = commentUiState.commentText,
                        commentViewModel = commentViewModel,
                        startPadding = 30,
                        parentCommentId = reply.id,
                        onDone = {
                            replyVisibilityId.value = ""
                            createComment(
                                comment = commentUiState,
                                postId = postId,
                                circleId = circleId,
                                addCommentToPost = addCommentToPost,
                            )
                            commentViewModel.resetComment()
                        }
                    )
                }
            }
        }
        //reset: for editing in preview
//              if (true){
        if (replyVisibilityId.value == "comment ${comment.id}"){
            val commentUiState by commentViewModel.uiState.collectAsState()
            CommentInput(
                value = commentUiState.commentText,
                commentViewModel = commentViewModel,
                startPadding = 0,
                parentCommentId = comment.id,
                onDone = {
                    replyVisibilityId.value = ""
                    createComment(
                        comment = commentUiState,
                        postId = postId,
                        circleId = circleId,
                        addCommentToPost = addCommentToPost,
                    )
                    commentViewModel.resetComment()
                }
            )
        }
    }
}

@Composable
fun CommentCard(
    comment: Comment,
    onClick: () -> Unit,
    startPadding: Dp = 0.dp,
){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = (0.5F)
        ),
        modifier = Modifier
            .padding(
                start = startPadding,
                bottom = 5.dp
            )
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 2.dp,
                    end = 2.dp,
                    top = 5.dp,
                    bottom = 5.dp,
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(6F)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.me_nav),
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .height(20.dp)
                            .width(20.dp),
                        contentDescription = "place holder for profile picture"
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = comment.attributes.authorDisplayName,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Text(
                    // reset: AnnotatedString breaks preview
//                            text = AnnotatedString.rememberAutoLinkText(comment.attributes.commentText),
                    text = comment.attributes.commentText,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 25.dp)
                )
                EmojiRow(
                    startPadding = 15.dp
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                ReplyIconButton(
                    onClick = {
                        onClick ()
                    }
                )
            }
        }

    }
}

@Composable
private fun ReplyIconButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.reply),
            contentDescription = "Reply",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .graphicsLayer {
                    scaleY = -1f // flip vertically
                }
        )
    }
}

@Composable
fun CommentInput(
    value: String,
    commentViewModel: CommentViewModel,
    startPadding: Int,
    parentCommentId: String,
    onDone: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = startPadding.dp,
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_reply),
            contentDescription = "Reply",
            tint = Color.DarkGray,
            modifier = Modifier
                .minimumInteractiveComponentSize()
        )

        Surface(
            shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
        ) {
            TextField(
                value = value,
                onValueChange = { newText ->   // Update the state with the new text
                    commentViewModel.setCommentText(newText)
                    commentViewModel.setParentCommentId(parentCommentId)
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
                    onDone()
                }),
                textStyle = TextStyle(color = Color.DarkGray)
            )
        }
    }
}

@Composable
fun FinalCommentCard(
    reply: Comment,
    replyVisibilityId: MutableState<String> = remember { mutableStateOf("") },
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, start = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_reply),
                contentDescription = "Reply",
                tint = Color.DarkGray,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
            )
        }

        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            color = Color.LightGray,
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 5.dp,
                        bottom = 5.dp
                    )
            ) {
                Text(
                    text = reply.attributes.authorDisplayName,
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    //reset: breaks preview
//                    text = AnnotatedString.rememberAutoLinkText(reply.attributes.commentText),
                    text = reply.attributes.commentText,
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically),

            contentAlignment = Alignment.CenterEnd
        ) {
            ReplyIconButton(
                onClick = {
                    val id = "reply ${reply.attributes.parentCommentId}"
                    replyVisibilityId.value = if (replyVisibilityId.value == id) "" else id
                }
            )
        }
    }
}


@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCommentCard() {
    val posts = SampleData.returnSamplePosts()
    val post = posts[0]
    val comments = post.attributes.comments.data
    ChatterBoxTheme {
        CommentAndRepliesCard(
            comment = comments[0],
            postId = post.id,
            circleId = post.attributes.circleId,
            addCommentToPost = {},
        )
    }
}