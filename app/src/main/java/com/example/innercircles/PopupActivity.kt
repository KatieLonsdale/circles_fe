//package com.example.innercircles
//
//import android.os.Build
//import android.os.Bundle
//import android.se.omapi.Session
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import coil.load
//import com.example.innercircles.api.data.Post
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import com.example.innercircles.api.RetrofitClient.apiService
//import com.example.innercircles.api.data.Comment
//import com.example.innercircles.api.data.CommentRequest
//import com.example.innercircles.api.data.CommentResponse
//import com.example.innercircles.api.data.PostResponse
//
//class PopupActivity : AppCompatActivity() {
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.post_dialog_fullscreen)
//
//        // Get the Post object passed from the adapter
////        TO DO: replaced deprecated code
//        val post = intent.getParcelableExtra<Post>("post")
//
//        // Reference UI elements
//        val popupImageView = findViewById<ImageView>(R.id.popupImageView)
//        val popupCaptionView = findViewById<TextView>(R.id.popupCaptionView)
//        val popupCommentsContainer = findViewById<LinearLayout>(R.id.popupCommentsContainer)
//        val newCommentEditText = findViewById<EditText>(R.id.newCommentEditText)
//        val addCommentButton = findViewById<Button>(R.id.addCommentButton)
//        val newCommentContainer = findViewById<LinearLayout>(R.id.newCommentContainer)
//
//        // Load image
//        post?.attributes?.contents?.data?.firstOrNull()?.attributes?.imageUrl?.let { url ->
//            popupImageView.load(url) {
//                error(R.drawable.error)
//            }
//        } ?: run {
//            popupImageView.setImageResource(R.drawable.error)
//        }
//
//        // Set caption
//        popupCaptionView.text = post?.attributes?.caption
//
//        // Populate commentsContainer
//        popupCommentsContainer.removeAllViews()
//        post?.attributes?.comments?.data?.forEach { comment ->
//            val commentView = layoutInflater.inflate(R.layout.comment_item, popupCommentsContainer, false)
//
//            val displayNameView = commentView.findViewById<TextView>(R.id.displayNameView)
//            val commentsView = commentView.findViewById<TextView>(R.id.commentsView)
//
//            displayNameView.text = comment.attributes.authorDisplayName
//            commentsView.text = comment.attributes.commentText
//
//            popupCommentsContainer.addView(commentView)
//        }
//
//        newCommentEditText.setOnClickListener {
//            newCommentEditText.requestFocus()
//            val commentText = newCommentEditText.text.toString().trim()
//            if (commentText.isNotEmpty()) {
//                addCommentButton.setOnClickListener {
//
//                val commentRequest = CommentRequest(commentText, null)
////                TODO: hardcoding null for parent_comment_id for now, feature will be added later
//                val userId = SessionManager.getUserId()
////                TODO: hardcoding circleid for now, need to add in a lookup, maybe in Session Manager?
//                val call = apiService.createComment(userId, "1", post?.id ?: "", commentRequest)
//                call.enqueue(object : Callback<CommentResponse> {
//                    override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
//                        if (response.isSuccessful) {
//                            newCommentEditText.text.clear()
////                            newCommentEditText.visibility = View.GONE
//                            addCommentButton.visibility = View.GONE
////                            newCommentButton.visibility = View.VISIBLE
//                        } else {
//                            Log.d("Upload", "Failed")
//                            Toast.makeText(this@PopupActivity, "New comment failed", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
//                        // Handle failure (e.g., show error)
//                    }
//                })
//                }
//            }
////            TODO: add message for user if comment is empty
//        }
//
//        newCommentEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
//            val originalAutofillHints = newCommentEditText.autofillHints
//            if (hasFocus) {
//                // Clear autofill hints and make add button visible when the EditText gains focus
//                newCommentEditText.setAutofillHints("")
//                newCommentEditText.isCursorVisible = true
//                addCommentButton.visibility = View.VISIBLE
//
//            } else {
//                // Restore autofill hints when the EditText loses focus
//                newCommentEditText.setAutofillHints(*originalAutofillHints)
//                newCommentEditText.isCursorVisible = false
//                addCommentButton.visibility = View.GONE
//            }
//        }
//
//    }
//}
