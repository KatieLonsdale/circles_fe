package com.katielonsdale.chatterbox.ui

import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.source.PostDataSource.createPost
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.ui.components.NewOptionIcon
import com.katielonsdale.chatterbox.ui.components.SelectChatters
import com.katielonsdale.chatterbox.ui.components.TextFieldOnSurface
import java.io.ByteArrayOutputStream


@Composable
fun NewPostScreen(
    currentUserChatters: List<Circle>,
    onClickPost: () -> Unit,
    newPostViewModel: NewPostViewModel,
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showError by remember { mutableStateOf(false) }
    val selectedChatterIds = remember {mutableStateListOf<String>()}
    val userChatters = currentUserChatters
    val newPostUiState by newPostViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    // Remove focus and dismiss keyboard when tapping outside the TextField
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            }
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.padding(
                10.dp
            )
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(
                            alpha = (0.5F)
                        )
                    )
                    .padding(
                        top = 20.dp,
                        bottom = 20.dp,
                        start = 10.dp,
                        end = 10.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    NewOptionIcon(
                        icon = R.drawable.new_post,
                        label = "Post"
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    MediaUploadButton(
                        onMediaSelected = {newPostViewModel.setContent(it)}
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ){
                    var caption by remember { mutableStateOf("") }
                    TextFieldOnSurface(
                        value = caption,
                        onValueChange = { newCaption ->
                            caption = newCaption
                            newPostViewModel.setCaption(newCaption)
                        },
                        label = "Write something...",
                        maxLines = 5,
                    )
                }

                if (showError) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Please add either an image or a caption",
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                        )
                ){
                    if (userChatters.isNotEmpty()) {
                        SelectChatters(
                            chatters = userChatters,
                            selectedChatterIds = selectedChatterIds,
                        )
                    } else {
                        Text(
                            text = "You don't belong to any Chatters! Please create or join a Chatter before posting.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                }

                if (userChatters.isNotEmpty() && selectedChatterIds.isNotEmpty()) {
                    Spacer(Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                if (newPostUiState.caption.isBlank() && newPostUiState.contents == null) {
                                    showError = true
                                } else {
                                    createPost(selectedChatterIds,newPostUiState)
                                    onClickPost()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                    alpha = 0.5F
                                ),
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                        ) {
                            Text(
                                text = "Post",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaUploadButton(
    onMediaSelected: (ContentViewModel) -> Unit
){
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val photoSelected = remember {mutableStateOf(false)}

    // Image Picker Launcher
    // todo: upgrade to photo picker https://developer.android.com/training/data-storage/shared/photopicker
    // todo: permission to access photos
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    if(!photoSelected.value) {

    Button(
        onClick = { launcher.launch("image/*") },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5F
            ),
                contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "Add Image",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    val byteArray = convertUriToByteArray(selectedImageUri, context)
    if (byteArray != null) {
        val contentViewModel = ContentViewModel()
        contentViewModel.setImage(byteArray)
        onMediaSelected(contentViewModel)
    }

    // Show selected image if available
    selectedImageUri?.let { uri ->
        photoSelected.value = true
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(uri)
                .size(Size.ORIGINAL)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = "Selected Image",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun convertUriToByteArray(uri: Uri?, context: Context): ByteArray? {
    val contentResolver = context.contentResolver
    var byteArray by remember { mutableStateOf<ByteArray?>(null) }

    // Run the coroutine only when the `uri` changes
    LaunchedEffect(uri) {
        if (uri != null) {
            byteArray = uri.toCompressedByteArray(context)
        }
    }
    return byteArray
}

/**
 * Decode the Uri into a Bitmap, resize it if it's wider than [maxWidth],
 * then JPEG-compress it at [quality]%, returning the resulting bytes.
 */
private suspend fun Uri.toCompressedByteArray(
    context: Context,
    maxWidth: Int = 1024,
    quality: Int = 50
): ByteArray? = withContext(Dispatchers.IO) {
    // 1) Decode
    val source = ImageDecoder.createSource(context.contentResolver, this@toCompressedByteArray)
    var bitmap = ImageDecoder.decodeBitmap(source)

    // 2) Resize if too big
    if (bitmap.width > maxWidth) {
        val ratio = maxWidth.toFloat() / bitmap.width
        val targetHeight = (bitmap.height * ratio).toInt()
        bitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, targetHeight, true)
    }

    // 3) Compress
    val output = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
    val bytes = output.toByteArray()

    // 4) Log the size
    Log.d("NewPostScreen", "Compressed image size: ${bytes.size} bytes " +
            "(${bytes.size / 1024f / 1024f} MB)")
    bytes
}


@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NewPostScreenPreview(){
    val userViewModel = UserViewModel()
    userViewModel.setCurrentUserChatters(SampleData.returnSampleChatters)
    ChatterBoxTheme {
        NewPostScreen(
            currentUserChatters = userViewModel.getCurrentUserChatters(),
            onClickPost = {},
            newPostViewModel = NewPostViewModel()
        )
    }
}
