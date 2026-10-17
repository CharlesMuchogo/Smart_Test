package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.domain.models.chat.BlockedUser
import com.charlesmuchogo.research.domain.models.chat.ChatMessage
import com.charlesmuchogo.research.domain.models.chat.Circle
import com.charlesmuchogo.research.domain.models.chat.CommunityProfile
import com.charlesmuchogo.research.domain.models.chat.Conversation
import com.charlesmuchogo.research.domain.models.chat.Doctor
import com.charlesmuchogo.research.domain.models.chat.SocketEvent
import com.charlesmuchogo.research.presentation.utils.Results
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {
    val events: SharedFlow<SocketEvent>

    val connected: StateFlow<Boolean>

    fun connect()

    fun disconnect()

    suspend fun getDoctors(): Results<List<Doctor>>

    suspend fun getConversations(): Results<List<Conversation>>

    suspend fun getConversation(id: Long): Results<Conversation>

    suspend fun startDoctorChat(doctorId: Long): Results<Conversation>

    suspend fun matchPeer(): Results<Conversation>

    suspend fun startDirectChat(userId: Long): Results<Conversation>

    suspend fun getMessages(conversationId: Long, before: Long? = null): Results<List<ChatMessage>>

    suspend fun sendMessage(conversationId: Long, clientId: String, body: String, resultId: Long? = null): Results<Unit>

    suspend fun markRead(conversationId: Long)

    suspend fun setMuted(conversationId: Long, muted: Boolean): Results<String>

    suspend fun getCircles(): Results<List<Circle>>

    suspend fun joinCircle(id: Long): Results<List<Circle>>

    suspend fun leaveCircle(id: Long): Results<List<Circle>>

    suspend fun reportMessage(messageId: Long, reason: String): Results<String>

    suspend fun blockUser(userId: Long): Results<String>

    suspend fun unblockUser(userId: Long): Results<String>

    suspend fun getBlockedUsers(): Results<List<BlockedUser>>

    suspend fun getCommunityProfile(): Results<CommunityProfile>

    suspend fun updateCommunityProfile(alias: String, messagePrivacy: String): Results<CommunityProfile>
}
