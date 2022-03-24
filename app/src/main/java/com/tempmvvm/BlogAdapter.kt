package com.tempmvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tempmvvm.databinding.ItemBlogBinding
import java.util.*

class BlogAdapter(private val onBlog: OnBlog) :
    ListAdapter<Blog, BlogAdapter.BlogViewHolder>(blogDiffUtil), Filterable {

    private lateinit var dataSet: ArrayList<Blog>

    private lateinit var fullList: List<Blog>

    fun addValues(blogs: List<Blog>) {

        dataSet = blogs as ArrayList<Blog>

        fullList = dataSet

    }

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

    private val filterBlogs: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {

            val charString = constraint.toString()
            fullList = if (charString.isEmpty()) dataSet else {
                val filteredList = ArrayList<Blog>()
                dataSet.filter { it.title.contains(constraint) }.forEach { filteredList.add(it) }
                filteredList
            }
            return FilterResults().apply { values = fullList }
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            results.values?.apply {
                fullList = results.values as ArrayList<Blog>
                submitList(fullList)
            }
        }

    }

    override fun getFilter(): Filter {
        return filterBlogs
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