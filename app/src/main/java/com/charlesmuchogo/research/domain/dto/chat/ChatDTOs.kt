package com.charlesmuchogo.research.domain.dto.chat

import com.charlesmuchogo.research.domain.models.chat.BlockedUser
import com.charlesmuchogo.research.domain.models.chat.ChatMessage
import com.charlesmuchogo.research.domain.models.chat.Circle
import com.charlesmuchogo.research.domain.models.chat.CommunityProfile
import com.charlesmuchogo.research.domain.models.chat.Conversation
import com.charlesmuchogo.research.domain.models.chat.Doctor
import kotlinx.serialization.Serializable

@Serializable
data class DoctorsResponse(val message: String = "", val doctors: List<Doctor>? = null)

@Serializable
data class ConversationsResponse(val message: String = "", val conversations: List<Conversation>? = null)

@Serializable
data class ConversationResponse(val message: String = "", val conversation: Conversation)

@Serializable
data class MessagesResponse(val message: String = "", val messages: List<ChatMessage>? = null)

@Serializable
data class ChatMessageResponse(val message: String = "", val data: ChatMessage)

@Serializable
data class CirclesResponse(val message: String = "", val circles: List<Circle>? = null)

@Serializable
data class CommunityProfileResponse(val message: String = "", val profile: CommunityProfile)

@Serializable
data class MessageResponse(val message: String = "")

@Serializable
data class StartDoctorChatRequest(val doctorId: Long)

@Serializable
data class StartDirectChatRequest(val userId: Long)

@Serializable
data class MuteRequest(val muted: Boolean)

@Serializable
data class ReportRequest(val reason: String)

@Serializable
data class SendChatMessageRequest(val clientId: String, val body: String, val resultId: Long? = null)

@Serializable
data class UpdateCommunityProfileRequest(val alias: String, val messagePrivacy: String)

@Serializable
data class BlockedUsersResponse(val message: String = "", val users: List<BlockedUser>? = null)
