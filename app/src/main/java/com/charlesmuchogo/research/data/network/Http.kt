package com.charlesmuchogo.research.data.network

import com.charlesmuchogo.research.data.local.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class Http(
    private val appDatabase: AppDatabase,
) {
    private var token: String? = null

    init {
        runBlocking {
            token =
                appDatabase
                    .userDao()
                    .getUser()
                    .firstOrNull()
                    ?.token
        }
    }

    val client by lazy {
        HttpClient(OkHttp.create {}) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 180_000L
                socketTimeoutMillis = 180_000L
                connectTimeoutMillis = 180_000L
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            }

            defaultRequest {
                url(httpUrlBuilder())
                token?.let { header("Authorization", token) }
            }
        }
    }

    companion object {
        fun httpUrlBuilder(): String = "https://smarttest.muchogoc.com"
    }
}
