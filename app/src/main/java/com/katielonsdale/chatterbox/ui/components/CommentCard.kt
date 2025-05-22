package com.katielonsdale.chatterbox.ui.components

import android.util.Log
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
import androidx.compose.ui.text.AnnotatedString
import com.katielonsdale.chatterbox.api.data.CommentUiState
import com.katielonsdale.chatterbox.utils.CommentCreator.createComment


@Composable
fun CommentCard(
    comment: Comment,
    postId: String,
    circleId: String,
    addCommentToPost: (Comment) -> Unit,
    commentViewModel: CommentViewModel = viewModel(),
    replyVisibilityId: MutableState<String> = remember { mutableStateOf("") },
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
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Combined author name and comment text
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
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
                        text = comment.attributes.authorDisplayName,
                        color = Color.DarkGray,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    SelectionContainer {
                        Text(
                        // reset: AnnotatedString breaks preview
                            text = AnnotatedString(comment.attributes.commentText),
//                            text = comment.attributes.commentText,
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
//                    .weight(0.1f)
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.CenterEnd
            ) {
                ReplyIconButton(
                    onClick = {
                        val id = "comment ${comment.id}"
                        replyVisibilityId.value = if (replyVisibilityId.value == id) "" else id

                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        val commentReplies = comment.attributes.replies?.data
        if (!commentReplies.isNullOrEmpty()) {
            val sortedReplies = commentReplies.sortedBy { it.attributes.createdAt }
            for (reply in sortedReplies) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box() {
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
                        shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
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

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                // reset: AnnotatedString breaks preview
                                text = AnnotatedString(reply.attributes.commentText),
//                                text = reply.attributes.commentText,
                                color = Color.DarkGray,
                                fontSize = 20.sp,
                            )
                        }
                    }

                    Box(
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        ReplyIconButton(
                            onClick = {
                                val id = "reply ${reply.id}"
                                replyVisibilityId.value =
                                    if (replyVisibilityId.value == id) "" else id
                            }
                        )
                    }
                }

                val secondCommentReplies = reply.attributes.replies?.data
                if (!secondCommentReplies.isNullOrEmpty()) {
                    val sortedSubReplies = secondCommentReplies.sortedBy { it.attributes.createdAt }
                    for (subReply in sortedSubReplies) {
                        Spacer(modifier = Modifier.height(5.dp))
                        FinalCommentCard(
                            reply = subReply,
                            replyVisibilityId = replyVisibilityId,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
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
private fun ReplyIconButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() },
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
                    text = AnnotatedString(reply.attributes.commentText),
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
    CommentCard(
        comment = comments[0],
        postId = post.id,
        circleId = post.attributes.circleId,
        addCommentToPost = {},
    )
}