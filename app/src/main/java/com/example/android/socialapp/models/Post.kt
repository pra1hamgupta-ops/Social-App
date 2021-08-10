package com.example.android.socialapp.models

data class Post(
    val text:String = "",
    val createdBy:User = User(),
    val createdAt:Long = 0L,
    val likes:ArrayList<String> = ArrayList()
) {
}