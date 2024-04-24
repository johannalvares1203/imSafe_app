package com.example.imsafeapp.chat.model

data class User(
    var userId: String = "",
    var userName: String = "",
    var profileImage: String = "",
    var online: Boolean = false,
    var role: String = "",
    var PhoneNumber: String = ""
)