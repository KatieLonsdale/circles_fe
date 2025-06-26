package com.katielonsdale.chatterbox.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Comment
import com.katielonsdale.chatterbox.api.data.CommentViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.utils.CommentCreator.createComment


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
    val commentUiState by commentViewModel.uiState.collectAsState()

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
            replyVisibilityId = replyVisibilityId,
            commentViewModel = commentViewModel,
            postId = postId,
            circleId = circleId,
            addCommentToPost = addCommentToPost
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
                    startPadding = 25.dp,
                    replyVisibilityId = replyVisibilityId,
                    commentViewModel = commentViewModel,
                    postId = postId,
                    circleId = circleId,
                    addCommentToPost = addCommentToPost
                )

                spacer

                val secondCommentReplies = reply.attributes.replies?.data
                if (!secondCommentReplies.isNullOrEmpty()) {
                    val sortedSubReplies = secondCommentReplies.sortedBy { it.attributes.createdAt }
                    for (subReply in sortedSubReplies) {
                        CommentCard(
                            comment = subReply,
                            onClick = {
                                val id = "subreply ${subReply.attributes.parentCommentId}"
                                replyVisibilityId.value = if (replyVisibilityId.value == id) "" else id
                            },
                            startPadding = 50.dp,
                            replyVisibilityId = replyVisibilityId,
                            commentViewModel = commentViewModel,
                            postId = postId,
                            circleId = circleId,
                            addCommentToPost = addCommentToPost
                        )

                        spacer

                        //reset: for editing in preview
//                                      if (true){
                        if (replyVisibilityId.value == "subreply ${subReply.attributes.parentCommentId}") {
                            CommentInput(
                                value = commentUiState.commentText,
                                commentViewModel = commentViewModel,
                                startPadding = 30,
                                parentComment = reply,
                                onDone = {
                                    replyVisibilityId.value = ""
                                    createComment(
                                        comment = commentUiState,
                                        postId = postId,
                                        circleId = circleId,
                                        addCommentToPost = addCommentToPost,
                                    )
                                    commentViewModel.resetComment()
                                },
                                finalComment = true
                            )
                        }
                    }
                }
                spacer
                //reset: for editing in preview
//              if (true){
                if (replyVisibilityId.value == "reply ${reply.id}") {
                    CommentInput(
                        value = commentUiState.commentText,
                        commentViewModel = commentViewModel,
                        startPadding = 30,
                        parentComment = reply,
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
    }
}

@Composable
fun CommentCard(
    comment: Comment,
    onClick: () -> Unit,
    startPadding: Dp = 0.dp,
    replyVisibilityId: MutableState<String>,
    commentViewModel: CommentViewModel,
    postId: String,
    circleId: String,
    addCommentToPost: (Comment) -> Unit,
){
    Column() {
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
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
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
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    ReplyIconButton(
                        onClick = {
                            onClick()
                        }
                    )
                }
            }
        }
        //reset: for editing in preview
//        if (true) {
        if (replyVisibilityId.value == "comment ${comment.id}"){
            Row(
                modifier = Modifier
                    .padding(
                        start = 15.dp,
                        bottom = 5.dp
                    )
            ) {
                val commentUiState by commentViewModel.uiState.collectAsState()
                CommentInput(
                    value = commentUiState.commentText,
                    commentViewModel = commentViewModel,
                    startPadding = 0,
                    parentComment = comment,
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
    parentComment: Comment,
    onDone: () -> Unit,
    finalComment: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 5.dp,
                start = startPadding.dp,
                bottom = 5.dp,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.reply),
            contentDescription = "Reply",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .rotate(180F)
        )

        Surface(
            shape = MaterialTheme.shapes.small,
        ) {
            TextField(
                value = value,
                onValueChange = { newText ->
                    commentViewModel.setCommentText(newText)
                    commentViewModel.setParentCommentId(parentComment.id)
                },
                placeholder = {
                    Text(
                        text = if(finalComment) "Reply in thread" else "Replying to ${parentComment.attributes.authorDisplayName}...",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                textStyle = MaterialTheme.typography.labelSmall,
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.background.copy(
                        alpha = (0.5F)
                    ),
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onDone()
                    Toast.makeText(
                        context,
                        "You replied to ${parentComment.attributes.authorDisplayName}'s comment",
                        Toast.LENGTH_LONG
                    ).show()
                }),
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