package com.example.imsafeapp.community.user

data class User(
    val userName: String?,
    val selectedConstituency: String?,
    val PhoneNumber: String?
) {
    constructor() : this(null, null, null)
}