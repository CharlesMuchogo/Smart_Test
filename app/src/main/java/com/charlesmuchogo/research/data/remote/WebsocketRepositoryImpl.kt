package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.network.Http
import com.charlesmuchogo.research.domain.models.Message
import io.ktor.client.plugins.websocket.ws
import io.ktor.client.plugins.websocket.wss
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class WebsocketRepositoryImpl(
    private val settingsRepository: MultiplatformSettingsRepository,
    private val dataSource: AppDatabase,
    private val http: Http,
) : WebsocketRepository {
    private var webSocketSession: WebSocketSession? = null
    private val reconnectDelayMillis = 5000L

    init {
        CoroutineScope(Dispatchers.IO).launch {
            syncUnsentMessages()
        }
    }

    private val json =
        Json {
            ignoreUnknownKeys = true
        }

    override suspend fun connectWebSocket() {
        println("Connecting to websocket")
        if (webSocketSession == null) {
            while (true) {
                try {
                    val userId = dataSource.userDao().getUser().firstOrNull()?.id

                    if (userId != null) {
                        http.wsClient.wss(urlString = "/chat/ai-chat?userId=$userId") {
                            println("Connected to websocket")
                            webSocketSession = this
                            for (frame in incoming) {
                                if (frame is Frame.Text) {
                                    val message = frame.readText()
                                    println("Received message -> $message")
                                    saveMessage(message = message)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Websocket disconnected because -> ${e.message}")
                    webSocketSession = null
                }
                delay(reconnectDelayMillis)
            }
        }
    }

    override suspend fun sendMessage(message: String) {
        println("Sending Message -> $message website is ${webSocketSession != null}")
        webSocketSession?.send(frame = Frame.Text(message))
    }

    override suspend fun disconnectWebSocket() {
        webSocketSession?.close()
        webSocketSession = null
        println("WebSocket disconnected manually.")
    }

    override suspend fun syncUnsentMessages() {
        dataSource.messagesDao().getUnsentMessages() .catch {
            it.printStackTrace()
            currentCoroutineContext().ensureActive()
        }.collect { messages ->
            messages.forEach { message ->
                val msg = if (message.id == message.timestamp) message.copy(id = null) else message
                sendMessage(message = json.encodeToString<Message>(msg))
            }
        }
    }

    private suspend fun saveMessage(message: String) {
        try {
            val msg = json.decodeFromString<Message>(message).copy(synced = true)
            dataSource.messagesDao().insertMessage(msg)
        } catch (e: Exception) {
            e.printStackTrace()
            currentCoroutineContext().ensureActive()
        }
    }
}
