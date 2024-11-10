//package com.example.innercircles
//
//import android.content.ContentResolver
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import com.example.innercircles.api.RetrofitClient.apiService
//import com.example.innercircles.api.data.PostRequest
//import com.example.innercircles.api.data.PostRequestContent
//import com.example.innercircles.api.data.PostResponse
//import com.example.innercircles.utils.PreferencesHelper
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import com.example.innercircles.utils.UploadManager
//import java.io.ByteArrayOutputStream
//import java.io.File
//import java.io.IOException
//
//class UploadActivity : AppCompatActivity() {
//
//    private lateinit var mediaPickerLauncher: ActivityResultLauncher<Intent>
//    private var selectedMediaUri: Uri? = null
//    private lateinit var preferencesHelper: PreferencesHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_upload)
//
//        val btnSelectMedia: Button = findViewById(R.id.btn_select_media)
//        val btnSubmit: Button = findViewById(R.id.btn_submit)
//        val btnBack: Button = findViewById(R.id.btn_back)
//        val editCaption: EditText = findViewById(R.id.edit_caption)
//
//        // Register the media picker launcher
//        mediaPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK && result.data != null) {
//                selectedMediaUri = result.data?.data
//                Toast.makeText(this, "Media selected: $selectedMediaUri", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        btnSelectMedia.setOnClickListener {
//            // Intent to pick either photo or video
//            val mediaPickerIntent = Intent(Intent.ACTION_PICK).apply {
//                type = "*/*"
//                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
//            }
//            mediaPickerLauncher.launch(mediaPickerIntent)
//        }
//
//        btnSubmit.setOnClickListener {
//            val caption = editCaption.text.toString()
//
//            if (selectedMediaUri != null || caption.isNotEmpty()) {
//                Toast.makeText(this, "Submitted with media: $selectedMediaUri and caption: $caption", Toast.LENGTH_SHORT).show()
//                uploadPost(caption, selectedMediaUri)
//            } else {
//                Toast.makeText(this, "Please upload a photo and/or add a caption", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        btnBack.setOnClickListener {
//            finish()  // Go back to the previous activity
//        }
//    }
//
//    private fun uploadMedia(){
//
//    }
//
//    private fun uploadPost(caption: String?, mediaUri: Uri?) {
//        preferencesHelper = PreferencesHelper(this)
//        val userId = preferencesHelper.getUserId()
//
//        if (userId == null) {
//            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//        val content = if (mediaUri == null) null else createPostRequestContent(mediaUri)
//        val postRequest = PostRequest(caption, content)
////        TODO: hard coding circle for now, need to add option to choose
//        val call = apiService.createPost(userId, "1", postRequest)
//
//        call.enqueue(object : Callback<PostResponse> {
//            override fun onResponse(
//                call: Call<PostResponse>,
//                response: Response<PostResponse>
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("Upload", "Success")
//                    Toast.makeText(this@UploadActivity, "Post created", Toast.LENGTH_SHORT).show()
//                    finish() // Optionally call finish if you want to close the activity
//                } else {
//                    Log.d("Upload", "Failed")
//                    Toast.makeText(this@UploadActivity, "New post failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
//                Log.e("Upload error:", t.message ?: "Unknown error")
//            }
//        })
//    }
//
//    private fun createPostRequestContent(mediaUri: Uri): PostRequestContent? {
//        val contentResolver: ContentResolver = contentResolver
//
//        try {
//            val inputStream = contentResolver.openInputStream(mediaUri)
//            val byteArray = inputStream?.readBytes()
//
//            return if (byteArray != null) {
//                PostRequestContent(byteArray, null) // Currently handling only images
//            } else {
//                null
//            }
//        } catch (e: IOException) {
//            Log.e("Upload", "Error creating post request content", e)
//            return null
//        }
//    }
//
//    private fun convertMediaToByteArray(media: Bitmap): ByteArray {
//        val stream = ByteArrayOutputStream()
//        media.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        return stream.toByteArray()
//    }
//}