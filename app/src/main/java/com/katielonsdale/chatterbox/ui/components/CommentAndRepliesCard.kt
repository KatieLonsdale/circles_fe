package com.katielonsdale.chatterbox.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.platform.LocalContext
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.utils.CommentCreator.createComment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.AnnotatedString
import sh.calvin.autolinktext.rememberAutoLinkText



@Composable
fun CommentAndRepliesCard(
    comment: Comment,
    postId: String,
    circleId: String,
    addCommentToPost: (Comment) -> Unit,
    replyVisibilityId: MutableState<String> = remember { mutableStateOf("") },
) {

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        //reset: for editing in preview
//        if (true) {
        if (replyVisibilityId.value == "comment ${comment.id}") {
            SelectedCommentCard(
                comment = comment,
                onClick = {
                    val id = "comment ${comment.id}"
                    replyVisibilityId.value = if (replyVisibilityId.value == id) "" else id
                },
            )

            Row(
                modifier = Modifier
                    .padding(
                        bottom = 5.dp
                    )
            ) {

                CommentInput(
                    parentComment = comment,
                    postId = postId,
                    circleId = circleId,
                    addCommentToPost = addCommentToPost,
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            val commentReplies = comment.attributes.replies.data
            if (commentReplies.count() > 0) {
                val sortedReplies = commentReplies.sortedBy { it.attributes.createdAt }
                val secondReplyVisibilityId = remember {mutableStateOf("")}

                CommentReplies(
                    commentReplies = sortedReplies,
                    postId = postId,
                    circleId = circleId,
                    addCommentToPost = addCommentToPost,
                    secondReplyVisibilityId = secondReplyVisibilityId,
                )
            }
        } else {
            CommentCard(
                comment = comment,
                onClick = {
                    val id = "comment ${comment.id}"
                    replyVisibilityId.value = if (replyVisibilityId.value == id) "" else id
                },
            )
        }
    }
}

@Composable
fun CommentCard(
    comment: Comment,
    onClick: () -> Unit,
){
    val commentReplies = comment.attributes.replies.data

    Column() {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = (0.5F)
            ),
            modifier = Modifier
                .padding(
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
                    .clickable{
                        onClick()
                    }
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
                        text = AnnotatedString.rememberAutoLinkText(comment.attributes.commentText),
//                        text = comment.attributes.commentText,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(start = 25.dp)
                    )
//                    EmojiRow(
//                        startPadding = 15.dp
//                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            onClick()
                        },
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(commentReplies.count() > 0) {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${commentReplies.count()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(2.dp))
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.chat,
                                ),
                                contentDescription = "replies",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedCommentCard(
    comment: Comment,
    onClick: () -> Unit,
){
    val commentReplies = comment.attributes.replies.data

    Column() {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.background.copy(
                alpha = 0.7F
            ),
            modifier = Modifier
                .padding(
                    bottom = 5.dp
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small,
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
                    .clickable{
                        onClick()
                    }
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
                        text = AnnotatedString.rememberAutoLinkText(comment.attributes.commentText),
//                        text = comment.attributes.commentText,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(start = 25.dp)
                    )
//                    EmojiRow(
//                        startPadding = 15.dp
//                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(commentReplies.count() > 0) {
                        val canvasColor = MaterialTheme.colorScheme.primary
                        Icon(
                            painter = painterResource(
                                id = R.drawable.chat,
                            ),
                            contentDescription = "replies",
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .size(25.dp)
                                .drawBehind {
                                    drawCircle (
                                        color = canvasColor,
                                        radius = 50F
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommentReplies(
    commentReplies: List<Comment>,
    postId: String,
    circleId: String,
    addCommentToPost: (Comment) -> Unit,
    secondReplyVisibilityId: MutableState<String>,
){

    Column(
        modifier = Modifier.padding(start = 25.dp)
    ) {
        for (reply in commentReplies) {
            //reset: for editing in preview
    //        if (true) {
            if (secondReplyVisibilityId.value == "reply ${reply.id}") {
                SelectedCommentCard(
                    comment = reply,
                    onClick = {
                        val id = "comment ${reply.id}"
                        secondReplyVisibilityId.value = if (secondReplyVisibilityId.value == id) "" else id
                    },
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier
                        .padding(
                            bottom = 5.dp
                        )
                ) {
                    CommentInput(
                        parentComment = reply,
                        postId = postId,
                        circleId = circleId,
                        addCommentToPost = addCommentToPost,
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                val secondCommentReplies = reply.attributes.replies.data
                if (secondCommentReplies.isNotEmpty()) {
                    val sortedSecondCommentReplies = secondCommentReplies.sortedBy { it.attributes.createdAt }
                    val thirdReplyVisibilityId = remember {mutableStateOf("")}
                    SecondCommentReplies(
                        secondCommentReplies = sortedSecondCommentReplies,
                        thirdReplyVisibilityId = thirdReplyVisibilityId,
                        comment = reply,
                        postId = postId,
                        circleId = circleId,
                        addCommentToPost = addCommentToPost,
                    )
                }
            } else {
                CommentCard(
                    comment = reply,
                    onClick = {
                        val id = "reply ${reply.id}"
                        secondReplyVisibilityId.value = if (secondReplyVisibilityId.value == id) "" else id
                    },
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
private fun SecondCommentReplies(
    secondCommentReplies: List<Comment>,
    thirdReplyVisibilityId: MutableState<String> = remember { mutableStateOf("") },
    comment: Comment,
    postId: String,
    circleId: String,
    addCommentToPost: (Comment) -> Unit,
){
    Column(
        modifier = Modifier.padding(start = 50.dp)
    ) {
        for (subReply in secondCommentReplies) {
            if (thirdReplyVisibilityId.value == "subreplies") {
                SelectedCommentCard(
                    comment = subReply,
                    onClick = {
                        val id = "subreplies"
                        thirdReplyVisibilityId.value = if (thirdReplyVisibilityId.value == id) "" else id
                    },
                )
            } else {
                CommentCard(
                    comment = subReply,
                    onClick = {
                        val id = "subreplies"
                        thirdReplyVisibilityId.value =
                            if (thirdReplyVisibilityId.value == id) "" else id
                    },
                )
            }
        }
            if (thirdReplyVisibilityId.value == "subreplies") {
                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier
                        .padding(
                            bottom = 5.dp
                        )
                ) {
                    CommentInput(
                        parentComment = comment,
                        finalComment = true,
                        postId = postId,
                        circleId = circleId,
                        addCommentToPost = addCommentToPost,
                        onDone = {
                            thirdReplyVisibilityId.value = ""
                        }
                    )
                }
            }
    }
}

@Composable
fun CommentInput(
    parentComment: Comment,
    finalComment: Boolean = false,
    postId: String,
    circleId: String,
    addCommentToPost: (Comment) -> Unit,
    onDone: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val commentViewModel: CommentViewModel = viewModel()
    val commentUiState by commentViewModel.uiState.collectAsState()
    val inputValue = remember{mutableStateOf("")}

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 5.dp,
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
                value = inputValue.value,
                onValueChange = { newText ->
                    inputValue.value = newText
                    commentViewModel.setCommentText(newText)
                    commentViewModel.setParentCommentId(parentComment.id)
                },
                placeholder = {
                    Text(
                        text = if(finalComment) "Reply" else "Replying to ${parentComment.attributes.authorDisplayName}...",
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
                    .fillMaxWidth()
                    .onFocusChanged {
                        commentViewModel.setCommentText("")
                        inputValue.value = ""
                    }
                    .focusable(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    inputValue.value = ""
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    if(finalComment) {onDone()}
                    createComment(
                        comment = commentUiState,
                        postId = postId,
                        circleId = circleId,
                        addCommentToPost = addCommentToPost,
                    )
                    commentViewModel.resetComment()
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
fun PreviewCommentAndRepliesCard() {
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

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSelectedCommentCard(){
    val posts = SampleData.returnSamplePosts()
    val post = posts[0]
    val comments = post.attributes.comments.data
    ChatterBoxTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        ) {
            SelectedCommentCard(
                comment = comments[0],
                onClick = {},
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCommentInput(){
    val posts = SampleData.returnSamplePosts()
    val post = posts[0]
    val comments = post.attributes.comments.data

    ChatterBoxTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        ) {
            CommentInput(
                parentComment = comments[0],
                postId = "1",
                circleId = "`",
                addCommentToPost = {},
            )
        }
    }
}