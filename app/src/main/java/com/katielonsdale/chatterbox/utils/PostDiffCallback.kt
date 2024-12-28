package com.katielonsdale.chatterbox.utils

import androidx.recyclerview.widget.DiffUtil
import com.katielonsdale.chatterbox.api.data.Post

class PostDiffCallback(
    private val oldList: List<Post>,
    private val newList: List<Post>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
}