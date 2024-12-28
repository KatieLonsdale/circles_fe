package com.katielonsdale.chatterbox.api.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.katielonsdale.chatterbox.R

class MyCirclesAdapter(private val circles: List<Circle>) : RecyclerView.Adapter<MyCirclesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.circle_name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.circle_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_circle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val circle = circles[position]
        holder.nameTextView.text = circle.attributes.name
        holder.descriptionTextView.text = circle.attributes.description
    }

    override fun getItemCount(): Int {
        return circles.size
    }
}
