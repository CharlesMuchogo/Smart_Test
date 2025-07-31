package com.charlesmuchogo.research.data.network

import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
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
    private val settingsRepository: MultiplatformSettingsRepository
) {
    private var token: String? = null

    init {
        runBlocking {
            token = settingsRepository.getAccessToken().firstOrNull()
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
                requestTimeoutMillis = 30_000L
                socketTimeoutMillis = 30_000L
                connectTimeoutMillis = 30_000L
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


            defaultRequest {
                url(httpUrlBuilder())
                token?.let { header("Authorization", token) }
            }
        }
    }

}

/*fun httpUrlBuilder(): String =  "http://192.168.100.59:9000"*/
fun httpUrlBuilder(): String =  "https://smarttest.muchogoc.com"
