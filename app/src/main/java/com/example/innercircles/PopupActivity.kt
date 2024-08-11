package com.example.innercircles

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.innercircles.api.data.Post

class PopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_dialog_fullscreen)

        // Get the Post object passed from the adapter
        val post = intent.getParcelableExtra<Post>("post")

        // Reference UI elements
        val popupImageView = findViewById<ImageView>(R.id.popupImageView)
        val popupCaptionView = findViewById<TextView>(R.id.popupCaptionView)
        val popupCommentsContainer = findViewById<LinearLayout>(R.id.popupCommentsContainer)
        val newCommentButton = findViewById<TextView>(R.id.newCommentButton)

        // Load image
        post?.attributes?.contents?.data?.firstOrNull()?.attributes?.imageUrl?.let { url ->
            popupImageView.load(url) {
                error(R.drawable.error)
            }
        } ?: run {
            popupImageView.setImageResource(R.drawable.error)
        }

        // Set caption
        popupCaptionView.text = post?.attributes?.caption

        // Populate commentsContainer
        popupCommentsContainer.removeAllViews()
        post?.attributes?.comments?.data?.forEach { comment ->
            val commentView = layoutInflater.inflate(R.layout.comment_item, popupCommentsContainer, false)

            val displayNameView = commentView.findViewById<TextView>(R.id.displayNameView)
            val commentsView = commentView.findViewById<TextView>(R.id.commentsView)

            displayNameView.text = comment.attributes.authorDisplayName
            commentsView.text = comment.attributes.commentText

            popupCommentsContainer.addView(commentView)
        }

        // Set new comment button click listener
        newCommentButton.setOnClickListener {
            // Implement the logic to add a new comment
        }

    }
}
