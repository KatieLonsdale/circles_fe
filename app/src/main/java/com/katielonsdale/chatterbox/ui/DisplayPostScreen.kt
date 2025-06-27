package com.katielonsdale.chatterbox.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Comment
import com.katielonsdale.chatterbox.api.data.CommentUiState
import com.katielonsdale.chatterbox.api.data.PostUiState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.katielonsdale.chatterbox.api.data.CommentViewModel
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.ui.components.CommentAndRepliesCard
import sh.calvin.autolinktext.rememberAutoLinkText
import com.katielonsdale.chatterbox.ui.components.formatTimeStamp
import com.katielonsdale.chatterbox.utils.CommentCreator.createComment

@Composable
fun DisplayPostScreen(
    post: PostUiState,
    addCommentToPost: (Comment) -> Unit,
){
    val contents = post.contents
    var hasContent = true;
    if (contents.isEmpty()) { hasContent = false }

    // Get the FocusManager and KeyboardController to manage focus and keyboard behavior
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val commentViewModel: CommentViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 5.dp,
                bottom = 5.dp,
            )
            .shadow(
                8.dp,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    // Remove focus and dismiss keyboard when tapping outside the TextField
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            }
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        for (content in contents) {
            val imageUrl = content.attributes.imageUrl
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_image_error_24dp),
                contentDescription = stringResource(R.string.description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
            )
        }
             Column(
                 modifier = Modifier
                     .padding(
                         start = 4.dp,
                         end = 4.dp,
                     )
             ) {
                 Row(
                     modifier = Modifier
                         .fillMaxWidth(),
                     verticalAlignment = Alignment.CenterVertically,
                 ) {
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
                         text = post.authorDisplayName,
                         color = MaterialTheme.colorScheme.onSurface,
                         style = MaterialTheme.typography.bodyMedium,
                         modifier = Modifier.alignByBaseline(),
                     )
                     Spacer(Modifier.width(5.dp))
                     Text(
                         text = formatTimeStamp(post.updatedAt),
                         color = MaterialTheme.colorScheme.onSurface,
                         style = MaterialTheme.typography.bodySmall.copy(
                             fontSize = 15.sp,
                         ),
                         modifier = Modifier.alignByBaseline(),
                         maxLines = 1,
                         overflow = TextOverflow.Ellipsis
                     )
                 }
                 Spacer(modifier = Modifier.width(10.dp))
                 Text(
                     // reset: annotatedString breaks previews
                     text = AnnotatedString.rememberAutoLinkText(post.caption),
//                     text = post.caption,
                     color = MaterialTheme.colorScheme.onSurface,
                     style = MaterialTheme.typography.bodySmall
                 )
             }

        Spacer(modifier = Modifier.height(10.dp))

        val comments = post.comments
        if (comments.isNotEmpty()) {
            val replyVisibilityId = remember { mutableStateOf("") }
            val sortedComments = comments.sortedBy { it.attributes.createdAt }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ) {
                sortedComments.forEach { comment ->
                    CommentAndRepliesCard(
                        comment = comment,
                        postId = post.id,
                        circleId = post.circleId,
                        addCommentToPost = addCommentToPost,
                        replyVisibilityId = replyVisibilityId,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .padding(
                    start = 5.dp,
                    end = 5.dp,
                    bottom = 5.dp,
                )
        ) {
            CommentInput(
                post = post,
                addCommentToPost = addCommentToPost
            )
        }
    }
}

@Composable
fun CommentInput(
    post: PostUiState,
    addCommentToPost: (Comment) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val inputValue = remember{mutableStateOf("")}
    val commentViewModel: CommentViewModel = viewModel()
    val commentUiState by commentViewModel.uiState.collectAsState()

    TextField(
        value = inputValue.value,
        onValueChange = { newComment ->
            inputValue.value = newComment
            commentViewModel.setCommentText(newComment)
        },
        placeholder = {
            Text(
                text = "Comment on ${post.authorDisplayName}'s post",
                style = MaterialTheme.typography.labelSmall,
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
            createComment(
                comment = commentUiState,
                postId = post.id,
                circleId = post.circleId,
                addCommentToPost = addCommentToPost,
            )
            commentViewModel.resetComment()
            Toast.makeText(
                context,
                "You commented on ${post.authorDisplayName}'s post",
                Toast.LENGTH_LONG
            ).show()
        }),
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun DisplayPostScreenPreview() {
    val posts = SampleData.returnSamplePosts()
    val post = posts[0]
    val postUiState = PostUiState(
        post.id,
        post.attributes.authorDisplayName,
        post.attributes.caption,
        post.attributes.contents.data,
        post.attributes.comments.data,
        post.attributes.createdAt,
        post.attributes.updatedAt
    )

    val commentUiState = CommentUiState()
    ChatterBoxTheme {
        DisplayPostScreen(
            postUiState,
            addCommentToPost = {},
        )
    }
}
