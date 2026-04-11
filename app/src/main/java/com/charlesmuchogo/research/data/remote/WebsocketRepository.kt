package com.charlesmuchogo.research.data.remote

interface WebsocketRepository {
    suspend fun connectWebSocket()

    suspend fun sendMessage(message: String)

    suspend fun disconnectWebSocket()

    suspend fun syncUnsentMessages()
}
