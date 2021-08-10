package com.example.android.socialapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.socialapp.dao.PostDao
import com.example.android.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions

import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IPostAdapter {
    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent: Intent = Intent(this, CreatePost::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()

        logout.setOnClickListener {
            recyclerView.visibility = View.GONE
            fab.visibility = View.GONE
            logout.visibility = View.INVISIBLE
            dialogBox.visibility = View.VISIBLE
        }

        yes.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.putExtra("snout",true)
            startActivity(intent)
            finish()
        }

        no.setOnClickListener {
            recyclerView.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            logout.visibility = View.VISIBLE
            dialogBox.visibility = View.GONE
        }


    }

    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postCollections = postDao.postsCollection
        val query = postCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }


    override fun onLikeClicked(postId: String) {
        postDao = PostDao()
        postDao.updatesLikes(postId)
    }
}