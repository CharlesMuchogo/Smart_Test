package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.network.ApiHelper
import com.charlesmuchogo.research.data.network.Http
import com.charlesmuchogo.research.domain.dto.ErrorDTO
import com.charlesmuchogo.research.domain.dto.chat.BlockedUsersResponse
import com.charlesmuchogo.research.domain.dto.chat.ChatMessageResponse
import com.charlesmuchogo.research.domain.dto.chat.CirclesResponse
import com.charlesmuchogo.research.domain.dto.chat.CommunityProfileResponse
import com.charlesmuchogo.research.domain.dto.chat.ConversationResponse
import com.charlesmuchogo.research.domain.dto.chat.ConversationsResponse
import com.charlesmuchogo.research.domain.dto.chat.DoctorsResponse
import com.charlesmuchogo.research.domain.dto.chat.MessageResponse
import com.charlesmuchogo.research.domain.dto.chat.MessagesResponse
import com.charlesmuchogo.research.domain.dto.chat.MuteRequest
import com.charlesmuchogo.research.domain.dto.chat.ReportRequest
import com.charlesmuchogo.research.domain.dto.chat.SendChatMessageRequest
import com.charlesmuchogo.research.domain.dto.chat.StartDirectChatRequest
import com.charlesmuchogo.research.domain.dto.chat.StartDoctorChatRequest
import com.charlesmuchogo.research.domain.dto.chat.UpdateCommunityProfileRequest
import com.charlesmuchogo.research.domain.models.chat.BlockedUser
import com.charlesmuchogo.research.domain.models.chat.ChatMessage
import com.charlesmuchogo.research.domain.models.chat.Circle
import com.charlesmuchogo.research.domain.models.chat.CommunityProfile
import com.charlesmuchogo.research.domain.models.chat.Conversation
import com.charlesmuchogo.research.domain.models.chat.Doctor
import com.charlesmuchogo.research.domain.models.chat.SocketCommand
import com.charlesmuchogo.research.domain.models.chat.SocketEvent
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.Results
import com.charlesmuchogo.research.presentation.utils.decodeExceptionMessage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ChatRepositoryImpl(
    private val http: Http,
    private val apiHelper: ApiHelper,
    private val settingsRepository: MultiplatformSettingsRepository,
) : ChatRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
    private var socketJob: Job? = null
    private var session: DefaultClientWebSocketSession? = null

    private val _events = MutableSharedFlow<SocketEvent>(extraBufferCapacity = 64)
    override val events: SharedFlow<SocketEvent> = _events.asSharedFlow()

    private val _connected = MutableStateFlow(false)
    override val connected: StateFlow<Boolean> = _connected.asStateFlow()

    override fun connect() {
        if (socketJob?.isActive == true) return
        socketJob = scope.launch {
            while (isActive) {
                val token = settingsRepository.getAccessToken().firstOrNull()
                if (token.isNullOrBlank()) {
                    delay(RECONNECT_DELAY)
                    continue
                }
                try {
                    http.wsClient.wss(urlString = "/chat/conversations?token=${token.encodeURLParameter()}") {
                        session = this
                        _connected.value = true
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                runCatching { json.decodeFromString<SocketEvent>(frame.readText()) }
                                    .onSuccess { _events.emit(it) }
                            }
                        }
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    session = null
                    _connected.value = false
                }
                delay(RECONNECT_DELAY)
            }
        }
    }

    override fun disconnect() {
        socketJob?.cancel()
        socketJob = null
        session = null
        _connected.value = false
    }

    private suspend inline fun <reified T : Any> request(crossinline call: suspend HttpClient.() -> HttpResponse): Results<T> {
        return try {
            val response = http.client.call()
            if (response.status != HttpStatusCode.OK) {
                val error = apiHelper.safeApiCall(response.status) { response.body<ErrorDTO>() }
                Results.error(error.data?.message ?: error.message ?: DEFAULT_ERROR)
            } else {
                apiHelper.safeApiCall(response.status) { response.body<T>() }
            }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }
    }

    private fun <T, R> Results<T>.map(transform: (T) -> R): Results<R> {
        val value = data
        return if (status == ResultStatus.SUCCESS && value != null) {
            Results(ResultStatus.SUCCESS, transform(value), null)
        } else {
            Results(status, null, message ?: DEFAULT_ERROR)
        }
    }

    override suspend fun getDoctors(): Results<List<Doctor>> =
        request<DoctorsResponse> { get("/api/mobile/doctors") }.map { it.doctors.orEmpty() }

    override suspend fun getConversations(): Results<List<Conversation>> =
        request<ConversationsResponse> { get("/api/mobile/conversations") }.map { it.conversations.orEmpty() }

    override suspend fun getConversation(id: Long): Results<Conversation> =
        request<ConversationResponse> { get("/api/mobile/conversations/$id") }.map { it.conversation }

    override suspend fun startDoctorChat(doctorId: Long): Results<Conversation> =
        request<ConversationResponse> {
            post("/api/mobile/conversations/doctor") {
                contentType(ContentType.Application.Json)
                setBody(StartDoctorChatRequest(doctorId))
            }
        }.map { it.conversation }

    override suspend fun matchPeer(): Results<Conversation> =
        request<ConversationResponse> { post("/api/mobile/conversations/peer") }.map { it.conversation }

    override suspend fun startDirectChat(userId: Long): Results<Conversation> =
        request<ConversationResponse> {
            post("/api/mobile/conversations/direct") {
                contentType(ContentType.Application.Json)
                setBody(StartDirectChatRequest(userId))
            }
        }.map { it.conversation }

    override suspend fun getMessages(conversationId: Long, before: Long?): Results<List<ChatMessage>> =
        request<MessagesResponse> {
            get("/api/mobile/conversations/$conversationId/messages") {
                before?.let { parameter("before", it) }
            }
        }.map { it.messages.orEmpty() }

    override suspend fun sendMessage(conversationId: Long, clientId: String, body: String, resultId: Long?): Results<Unit> {
        val activeSession = session
        if (activeSession != null) {
            val command = SocketCommand(type = "message", conversationId = conversationId, clientId = clientId, body = body, resultId = resultId)
            val sent = runCatching { activeSession.send(Frame.Text(json.encodeToString(command))) }.isSuccess
            if (sent) return Results(ResultStatus.SUCCESS, Unit, null)
        }

        val result = request<ChatMessageResponse> {
            post("/api/mobile/conversations/$conversationId/messages") {
                contentType(ContentType.Application.Json)
                setBody(SendChatMessageRequest(clientId = clientId, body = body, resultId = resultId))
            }
        }
        result.data?.let { _events.emit(SocketEvent(type = "message", message = it.data)) }
        return result.map { }
    }

    override suspend fun markRead(conversationId: Long) {
        val activeSession = session
        if (activeSession != null) {
            val sent = runCatching {
                activeSession.send(Frame.Text(json.encodeToString(SocketCommand(type = "read", conversationId = conversationId))))
            }.isSuccess
            if (sent) return
        }
        request<MessageResponse> { post("/api/mobile/conversations/$conversationId/read") }
    }

    override suspend fun setMuted(conversationId: Long, muted: Boolean): Results<String> =
        request<MessageResponse> {
            post("/api/mobile/conversations/$conversationId/mute") {
                contentType(ContentType.Application.Json)
                setBody(MuteRequest(muted))
            }
        }.map { it.message }

    override suspend fun getCircles(): Results<List<Circle>> =
        request<CirclesResponse> { get("/api/mobile/circles") }.map { it.circles.orEmpty() }

    override suspend fun joinCircle(id: Long): Results<List<Circle>> =
        request<CirclesResponse> { post("/api/mobile/circles/$id/join") }.map { it.circles.orEmpty() }

    override suspend fun leaveCircle(id: Long): Results<List<Circle>> =
        request<CirclesResponse> { delete("/api/mobile/circles/$id/join") }.map { it.circles.orEmpty() }

    override suspend fun reportMessage(messageId: Long, reason: String): Results<String> =
        request<MessageResponse> {
            post("/api/mobile/chat-messages/$messageId/report") {
                contentType(ContentType.Application.Json)
                setBody(ReportRequest(reason))
            }
        }.map { it.message }

    override suspend fun blockUser(userId: Long): Results<String> =
        request<MessageResponse> { post("/api/mobile/users/$userId/block") }.map { it.message }

    override suspend fun unblockUser(userId: Long): Results<String> =
        request<MessageResponse> { delete("/api/mobile/users/$userId/block") }.map { it.message }

    override suspend fun getBlockedUsers(): Results<List<BlockedUser>> =
        request<BlockedUsersResponse> { get("/api/mobile/blocked-users") }.map { it.users.orEmpty() }

    override suspend fun getCommunityProfile(): Results<CommunityProfile> =
        request<CommunityProfileResponse> { get("/api/mobile/community/profile") }.map { it.profile }

    override suspend fun updateCommunityProfile(alias: String, messagePrivacy: String): Results<CommunityProfile> =
        request<CommunityProfileResponse> {
            put("/api/mobile/community/profile") {
                contentType(ContentType.Application.Json)
                setBody(UpdateCommunityProfileRequest(alias = alias, messagePrivacy = messagePrivacy))
            }
        }.map { it.profile }

    private companion object {
        const val RECONNECT_DELAY = 5_000L
        const val DEFAULT_ERROR = "Something went wrong. Try again"
    }
}
