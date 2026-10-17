package com.charlesmuchogo.research.domain.models.chat

import kotlinx.serialization.Serializable

object ConversationType {
    const val DOCTOR = "doctor"
    const val PEER = "peer"
    const val CIRCLE = "circle"
}

object MessageKind {
    const val TEXT = "text"
    const val RESULT = "result"
}

object MessagePrivacy {
    const val DOCTORS_ONLY = "doctors"
    const val CIRCLES = "circles"
    const val NONE = "none"
}

object ChatRole {
    const val DOCTOR = "doctor"
    const val PEER = "peer"
}

@Serializable
data class Doctor(
    val userId: Long,
    val name: String,
    val title: String = "",
    val specialty: String = "",
    val languages: String = "",
    val bio: String = "",
    val photo: String = "",
    val available: Boolean = false,
    val verified: Boolean = false,
) {
    val displayName: String
        get() = listOf(title, name).filter { it.isNotBlank() }.joinToString(" ")
}

@Serializable
data class ChatParticipant(
    val userId: Long,
    val name: String,
    val role: String = "",
    val verified: Boolean = false,
)

@Serializable
data class Conversation(
    val id: Long,
    val type: String,
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val lastMessage: String = "",
    val lastMessageAt: String? = null,
    val unread: Int = 0,
    val muted: Boolean = false,
    val verified: Boolean = false,
    val members: Int = 0,
    val participants: List<ChatParticipant> = emptyList(),
)

@Serializable
data class SharedResult(
    val id: Long,
    val results: String = "",
    val partnerResults: String = "",
    val status: String = "",
    val date: String = "",
    val image: String = "",
)

@Serializable
data class ChatMessage(
    val id: Long = 0,
    val conversationId: Long,
    val senderId: Long,
    val clientId: String = "",
    val kind: String = MessageKind.TEXT,
    val body: String = "",
    val resultId: Long? = null,
    val createdAt: String = "",
    val senderName: String = "",
    val senderRole: String = "",
    val result: SharedResult? = null,
    val pending: Boolean = false,
    val failed: Boolean = false,
)

@Serializable
data class Circle(
    val id: Long,
    val slug: String = "",
    val title: String,
    val description: String = "",
    val members: Int = 0,
    val joined: Boolean = false,
)

@Serializable
data class CommunityProfile(
    val userId: Long = 0,
    val role: String = "",
    val alias: String = "",
    val messagePrivacy: String = MessagePrivacy.CIRCLES,
)

@Serializable
data class SocketEvent(
    val type: String,
    val message: ChatMessage? = null,
    val error: String? = null,
)

@Serializable
data class SocketCommand(
    val type: String,
    val conversationId: Long,
    val clientId: String = "",
    val body: String = "",
    val resultId: Long? = null,
)

@Serializable
data class BlockedUser(
    val userId: Long,
    val name: String = "",
    val role: String = "",
    val blockedAt: String? = null,
)
