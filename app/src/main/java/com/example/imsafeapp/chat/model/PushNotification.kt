package com.example.imsafeapp.chat.model

import com.example.imsafeapp.chat.model.NotificationData

data class PushNotification(
    var data: NotificationData,
    var to: String
)