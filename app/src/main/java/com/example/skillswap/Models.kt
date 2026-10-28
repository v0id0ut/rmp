package com.example.skillswap

data class SkillItem(
    val id: Int,
    val title: String,
    val category: String,
    val format: String,
    val duration: String,
    val wantInReturn: String,
    val authorName: String,
    val authorInitials: String,
    val rating: Double,
    val reviewsCount: Int,
    val swapsCount: Int,
    val descriptionTitle: String,
    val descriptionText: String,
    val wantDetails: String,
    val nextMeeting: String
)

data class SwapRequest(
    val id: Int,
    val partnerName: String,
    val partnerInitials: String,
    val skillTitle: String,
    val inReturnTitle: String,
    val dateInfo: String,
    val status: String, // "ПОДТВЕРЖДЁН" или "ЖДЁМ ОТВЕТА"
    val isCompleted: Boolean = false
)

data class ChatThread(
    val id: Int,
    val partnerName: String,
    val partnerInitials: String,
    val skillTopic: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int
)

data class ChatMessage(
    val text: String,
    val time: String,
    val isFromMe: Boolean
)

data class ReviewItem(
    val author: String,
    val text: String,
    val date: String,
    val rating: Int
)