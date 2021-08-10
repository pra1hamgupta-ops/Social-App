package com.example.android.socialapp.dao

import com.example.android.socialapp.models.Post
import com.example.android.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    val postsCollection = db.collection("posts")
    private val auth = Firebase.auth


    fun addPost(text:String){
        val userId = auth.currentUser!!.uid

        GlobalScope.launch(Dispatchers.IO){
            val userDao = UserDao()
            val user: User = userDao.getUserById(userId).await().toObject(User::class.java)!!
            val time = System.currentTimeMillis()
            val post = Post(text,user, time)
            postsCollection.document().set(post)
        }
    }

    private fun getPostById(postId:String): Task<DocumentSnapshot> {
        return postsCollection.document(postId).get()
    }


    fun updatesLikes(postId:String){

        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likes.contains(currentUserId)

            if(isLiked){
                post.likes.remove(currentUserId)
            }
            else{
                post.likes.add(currentUserId)
            }

            postsCollection.document(postId).set(post)
        }
    }
}