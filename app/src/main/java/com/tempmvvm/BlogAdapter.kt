package com.tempmvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tempmvvm.databinding.ItemBlogBinding
import java.util.*

class BlogAdapter(private val onBlog: OnBlog) :
    ListAdapter<Blog, BlogAdapter.BlogViewHolder>(blogDiffUtil) {

    inner class BlogViewHolder(private var itemBlogBind: ItemBlogBinding) :
        RecyclerView.ViewHolder(itemBlogBind.root) {

        fun loadData(blog: Blog) {

            with(itemBlogBind) {

                tvTitle.text = blog.title

                ivDeleteBlog.setOnClickListener {
                    onBlog.onDeleted(blog)
                }

                ivEditBlog.setOnClickListener {
                    onBlog.onEdit(blog)
                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        return BlogViewHolder(
            ItemBlogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.loadData(getItem(position))
    }

}

val blogDiffUtil = object : DiffUtil.ItemCallback<Blog>() {

    override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem.blogId == newItem.blogId
    }

    override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return Objects.deepEquals(oldItem, newItem)
    }

}