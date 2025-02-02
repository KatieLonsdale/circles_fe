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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.api.data.NewPostUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun NewPostScreen(
    circleId: String,
    newPostUiState: NewPostUiState,
    onCaptionChanged: (String) -> Unit = {},
    onMediaSelected: (ContentViewModel) -> Unit = {},
    onClickNext: () -> Unit = {},
    onClickBack: () -> Unit = {}
){
    // Get the FocusManager and KeyboardController to manage focus and keyboard behavior
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
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
            },
            focusManager,
            keyboardController
        )
    }

        Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier
            .fillMaxSize() // Make the Box fill the entire screen
    ) {
        ElevatedButton(
            onClick = onClickNext,
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
    val scope = rememberCoroutineScope()
    var byteArray by remember { mutableStateOf<ByteArray?>(null) }

    // Run the coroutine only when the `uri` changes
    LaunchedEffect(uri) {
        if (uri != null) {
            byteArray = withContext(Dispatchers.IO) {
                val inputStream = contentResolver.openInputStream(uri)
                inputStream?.readBytes()
            }
        }
    }
    return byteArray
}

@Preview(showBackground = true)
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

//todo: add error handling if image and caption are blank