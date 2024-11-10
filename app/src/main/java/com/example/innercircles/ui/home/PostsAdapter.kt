//package com.example.innercircles.ui.home
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import coil.load
//import com.example.innercircles.PopupActivity
//import com.example.innercircles.R
//import com.example.innercircles.api.data.Post
//
//class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {
//
//    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.imageView)
//        val captionView: TextView = itemView.findViewById(R.id.captionView)
//        val commentsContainer: LinearLayout = itemView.findViewById<LinearLayout>(R.id.commentsContainer)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
//        return PostViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
//        val post = posts[position]
//        val content = post.attributes.contents.data.firstOrNull()
//
//        // Load image or show error placeholder
//        content?.attributes?.imageUrl?.let { url ->
//            holder.imageView.load(url) {
//                error(R.drawable.error) // Optional error image
//            }
//        } ?: run {
//            holder.imageView.setImageResource(R.drawable.error)
//        }
//
//        // Set caption
//        holder.captionView.text = post.attributes.caption
//
//        // Clear existing views in commentsContainer to avoid duplication
//        holder.commentsContainer.removeAllViews()
//
//        // Populate commentsContainer with username and comment pairs
//        val comments = post.attributes.comments.data
//        comments.forEach { comment ->
//            val commentView = LayoutInflater.from(holder.itemView.context)
//                .inflate(R.layout.comment_item, holder.commentsContainer, false)
//
//            // Find and set the username and comment TextViews within the inflated layout
//            val displayNameView = commentView.findViewById<TextView>(R.id.displayNameView)
//            val commentsView = commentView.findViewById<TextView>(R.id.commentsView)
//
//            displayNameView.text = comment.attributes.authorDisplayName
//            commentsView.text = comment.attributes.commentText
//
//            // Add the commentView to commentsContainer
//            holder.commentsContainer.addView(commentView)
//        }
//
//        // Set click listener to launch PopupActivity
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, PopupActivity::class.java)
//            intent.putExtra("post", post)
//            holder.itemView.context.startActivity(intent)
//        }
//    }
//
//    override fun getItemCount() = posts.size
//
//}
