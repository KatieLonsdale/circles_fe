package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.Comment
import com.katielonsdale.chatterbox.api.data.CommentRequest
import com.katielonsdale.chatterbox.api.data.CommentResponse
import com.katielonsdale.chatterbox.api.data.CommentUiState
import com.katielonsdale.chatterbox.api.data.PostUiState
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.katielonsdale.chatterbox.api.data.CommentAttributes

@Composable
fun DisplayPostScreen(
    post: PostUiState,
    comment: CommentUiState,
    onClickBack: () -> Unit,
    onCommentChanged: (String) -> Unit,
    addCommentToPost: (Comment) -> Unit,
    clearComment: () -> Unit
){
    val contents = post.contents
    var hasContent = true;
    if (contents.isEmpty()) { hasContent = false }

    // Get the FocusManager and KeyboardController to manage focus and keyboard behavior
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var counter by remember { mutableStateOf(0) }

    Column(
        Modifier
            .padding(top = 10.dp)
            .verticalScroll(rememberScrollState()) // Enable vertical scrolling
    ) {
        // Box for back arrow
        Box() {
            IconButton(
                onClick = {
                    onClickBack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), // Use your back arrow drawable
                    contentDescription = "Back",
                    modifier = Modifier.align(Alignment.TopStart),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 50.dp
                )
                .shadow(
                    8.dp,
                    shape = RoundedCornerShape(16.dp)
                )  // Apply shadow with rounded corners
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)  // Set background color (e.g., white),
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        // Remove focus and dismiss keyboard when tapping outside the TextField
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
        },
            Arrangement.Center,
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
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            val textSize: Int = if (!hasContent) {
                25
            } else {
                15
            }

            Row(
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp)
            ) {
                Text(
                    text = post.authorDisplayName,
                    color = Color.DarkGray,
                    fontSize = textSize.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = post.caption,
                    color = Color.DarkGray,
                    fontSize = textSize.sp,
                )
            }

            val comments = post.comments
            if (comments.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    comments.forEach { comment ->
                        CommentCard(comment)
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            CommentInput(
                onCommentChanged = { newComment ->   // Call setCaption on text change
                    onCommentChanged(newComment)
                },
                focusManager = focusManager,
                keyboardController = keyboardController,
                comment
            )

            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
            ) {
                ElevatedButton(
                    onClick = {
                        createComment(
                            comment,
                            post,
                            addCommentToPost
                        )
                        counter++
                        clearComment()
                    },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.BottomEnd),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // Background color
                        contentColor = Color.DarkGray,  // Text color
                    ),
                ) {
                    Text("Post")
                }
            }
        }
    }
}

@Composable
fun CommentCard(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            text = comment.attributes.authorDisplayName,
            color = Color.DarkGray,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = comment.attributes.commentText,
            color = Color.DarkGray,
            fontSize = 15.sp,
        )
    }
}

@Composable
fun CommentInput(
    onCommentChanged: (String) -> Unit,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    comment: CommentUiState
) {
    val value = ""
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        TextField(
            value = comment.commentText,
            onValueChange = { newText ->   // Update the state with the new text
                onCommentChanged(newText)
            },
            placeholder = { Text("Add a comment") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onCommentChanged(value)
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            textStyle = TextStyle(color = Color.DarkGray)
        )
    }
}

private fun createComment(
    comment: CommentUiState,
    post: PostUiState,
    addCommentToPost: (Comment) -> Unit,
    ){
    val userId = SessionManager.getUserId()
    val circleId = post.circleId
    val postId = post.id
    val commentRequest = createCommentRequest(comment)

    apiService.createComment(userId, circleId, postId, commentRequest).enqueue(object : Callback<CommentResponse>{
        override fun onResponse(
            call: Call<CommentResponse>,
            response: Response<CommentResponse>
        ) {
            if (response.isSuccessful) {
                val commentResponse = response.body()?.data
                val commentResponseAttributes = commentResponse?.attributes
                val commentAttributes = CommentAttributes(
                    commentResponse?.id ?: "",
                    commentResponseAttributes?.commentText ?: "",
                    commentResponseAttributes?.authorId ?: "",
                    commentResponseAttributes?.createdAt ?: "",
                    commentResponseAttributes?.updatedAt ?: "",
                    commentResponseAttributes?.authorDisplayName ?: "",
                    commentResponseAttributes?.parentCommentId ?: "",
                )
                val newComment = Comment(
                    id = commentResponse?.id ?: "",
                    attributes = commentAttributes
                )
                addCommentToPost(newComment)
                Log.e(
                    "DisplayPostScreen",
                    "Comment created successfully: ${response.body()}"
                )
            } else {
                Log.e(
                    "DisplayPostScreen",
                    "Failed to create comment: ${response.body()} status: ${response.code()}"
                )
            }
        }

        override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
            Log.e("SelectCirclesScreen", "Error creating comment", t)
        }
    })
}

private fun createCommentRequest(comment: CommentUiState): CommentRequest {
    val commentText = comment.commentText
    val parentCommentId = comment.parentCommentId
    val commentRequest = CommentRequest(
        commentText,
        parentCommentId
    )
    return commentRequest
}

@Preview(showBackground = true)
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

    DisplayPostScreen(
        postUiState,
        commentUiState,
        onClickBack= {},
        onCommentChanged = {},
        addCommentToPost = {},
        clearComment = {}
    )
}
