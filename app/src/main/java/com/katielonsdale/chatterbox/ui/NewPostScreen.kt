package com.katielonsdale.chatterbox.ui

import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.katielonsdale.chatterbox.api.data.NewPostUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.util.Log
import java.io.ByteArrayOutputStream


@Composable
fun NewPostScreen(
    circleId: String,
    newPostUiState: NewPostUiState,
    onCaptionChanged: (String) -> Unit = {},
    onMediaSelected: (ContentViewModel) -> Unit = {},
    onClickNext: () -> Unit = {},
){
    // Get the FocusManager and KeyboardController to manage focus and keyboard behavior
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    // Remove focus and dismiss keyboard when tapping outside the TextField
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ){
        Text(
            text = "New Post",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        MediaUploadButton(onMediaSelected)

        Spacer(modifier = Modifier.height(16.dp))

        CaptionInput(
            value = newPostUiState.caption,
            onCaptionChanged = { newCaption ->   // Call setCaption on text change
                onCaptionChanged(newCaption)
                showError = false // Clear error when user types
            },
            focusManager,
            keyboardController
        )

        if (showError) {
            Text(
                text = "Please add either an image or a caption",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier
            .fillMaxSize() // Make the Box fill the entire screen
    ) {
        ElevatedButton(
            onClick = {
                if (newPostUiState.caption.isBlank() && newPostUiState.contents == null) {
                    showError = true
                } else {
                    onClickNext()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray, // Background color
                contentColor = Color.DarkGray,  // Text color
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd) // Align to the bottom-right
                .padding(16.dp) // Padding to prevent the button from touching the screen edge
        ) {
            Text("Next")
        }
    }
}

@Composable
fun MediaUploadButton(
    onMediaSelected: (ContentViewModel) -> Unit
){
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Image Picker Launcher
    // todo: upgrade to photo picker https://developer.android.com/training/data-storage/shared/photopicker
    // todo: permission to access photos?
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    ElevatedButton(
        onClick = { launcher.launch("image/*") },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray, // Background color
            contentColor = Color.DarkGray  // Text color
        )
    ) {
        Text("Upload Image")
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
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(uri)
                .size(Size.ORIGINAL) // Use original size of the image
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
fun CaptionInput(
    value: String,
    onCaptionChanged: (String) -> Unit,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = value,
            onValueChange = { newText ->   // Update the state with the new text
                onCaptionChanged(newText)
            },
            placeholder = { Text("Add a caption") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            // Custom background
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            textStyle = TextStyle(color = Color.DarkGray) // Custom text style
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
    val newPostUiState = NewPostUiState()
    NewPostScreen(
        circleId = "1",
        newPostUiState = newPostUiState,
        onCaptionChanged = {},
        onMediaSelected = {},
        onClickNext = {}
    )
}
