package com.example.imsafeapp.community.user

data class Request(
    val userId: String?,
    val constituency: String?,
    val userName: String?,
    val phoneNumber: String?,
    val approved: Boolean?
) {
    // Add a default (no-argument) constructor
    constructor() : this("", "", "", "", false)
}