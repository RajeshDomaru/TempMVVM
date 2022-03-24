package com.tempmvvm

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tempmvvm.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    private val blogViewModel by viewModels<BlogViewModel>()

    private lateinit var blogAdapter: BlogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)

        setContentView(bind.root)

        load()

    }

    private fun load() {

        setOnclickListeners()

        loadAdapter()

        observers()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setOnclickListeners() {

        with(bind) {

            btnAddOrUpdate.setOnClickListener {

                if (etTitle.text.toString().trim().isNotEmpty()) {

                    if (btnAddOrUpdate.text.toString() == getString(R.string.add)) {

                        val blog =
                            Blog(UUID.randomUUID().toString(), etTitle.text.toString().trim())

                        blogViewModel.addBlog(blog)

                    } else {

                        val blog = Blog(tvBlogId.text.toString(), etTitle.text.toString().trim())

                        blogViewModel.updateBlog(blog)

                    }

                    resetFlow()

                } else {

                    etTitle.error = "Required!"

                }

            }

            btnCancel.setOnClickListener {
                resetFlow()
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.menuSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                blogAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                blogAdapter.filter.filter(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun resetFlow() {

        with(bind) {

            tvBlogId.text = null

            etTitle.text = null

            btnAddOrUpdate.text = getString(R.string.add)

            btnCancel.visibility = View.GONE

        }

    }

    private fun loadAdapter() {

        with(bind) {

            rvBlog.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)

            blogAdapter = BlogAdapter(object : OnBlog {

                override fun onDeleted(blog: Blog) {

                    blogViewModel.removeBlog(blog)

                }

                override fun onEdit(blog: Blog) {

                    tvBlogId.text = blog.blogId

                    etTitle.setText(blog.title)

                    etTitle.setSelection(blog.title.length)

                    btnAddOrUpdate.text = getString(R.string.update)

                    btnCancel.visibility = View.VISIBLE

                }

            })

            rvBlog.adapter = blogAdapter

        }

    }

    private fun observers() {

        blogViewModel.blogs.observe(this) {

            blogAdapter.addValues(it)

            blogAdapter.submitList(it.toMutableList())

        }

    }

}