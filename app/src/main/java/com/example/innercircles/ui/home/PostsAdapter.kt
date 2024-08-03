package com.example.innercircles.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.innercircles.R
import com.example.innercircles.api.data.Post

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val captionView: TextView = itemView.findViewById(R.id.captionView)
        val commentsView: TextView = itemView.findViewById(R.id.commentsView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        val content = post.attributes.contents.data.firstOrNull()
        content?.attributes?.imageUrl?.let { url ->
            holder.imageView.load(url) {
//                placeholder(R.drawable.placeholder) // Optional placeholder
//                error(R.drawable.error) // Optional error image
            }
        }
        holder.captionView.text = post.attributes.caption
        holder.commentsView.text = post.attributes.comments.data.joinToString("\n") { it.attributes.content }
    }

    override fun getItemCount() = posts.size
}
