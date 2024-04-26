package com.example.imsafeapp.chat.model

data class Chat(
    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var type: String = "text",
    var time: String = "",
    val fileName: String = ""
)

