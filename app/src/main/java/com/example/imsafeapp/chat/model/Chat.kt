package com.example.imsafeapp

data class Chat(
    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var type: String = "text",
    val fileName: String = ""
)

