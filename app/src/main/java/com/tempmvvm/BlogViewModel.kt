package com.tempmvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlogViewModel : ViewModel() {

    private var newBlogs = mutableListOf<Blog>()

    private var _blogs = MutableLiveData<MutableList<Blog>>()

    val blogs: LiveData<MutableList<Blog>> get() = _blogs

    fun addBlog(blog: Blog) {

        newBlogs = blogs.value ?: mutableListOf()

        newBlogs.add(blog)

        _blogs.value = newBlogs

    }

    fun updateBlog(blog: Blog) {

        newBlogs = blogs.value ?: mutableListOf()

        val updatedBlog = newBlogs.map {
            return@map if (it.blogId == blog.blogId)
                it.copy(blogId = blog.blogId, title = blog.title)
            else it
        }.toMutableList()

        _blogs.value = updatedBlog

    }

    fun removeBlog(blog: Blog) {

        newBlogs = blogs.value ?: mutableListOf()

        newBlogs.remove(blog)

        _blogs.value = newBlogs

    }

}