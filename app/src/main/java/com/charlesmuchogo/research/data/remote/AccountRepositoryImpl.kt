package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.data.network.ApiHelper
import com.charlesmuchogo.research.data.network.Http
import com.charlesmuchogo.research.domain.dto.ErrorDTO
import com.charlesmuchogo.research.domain.dto.account.SaveSubscriptionRequest
import com.charlesmuchogo.research.domain.dto.account.SubscriptionResponse
import com.charlesmuchogo.research.presentation.utils.Results
import com.charlesmuchogo.research.presentation.utils.decodeExceptionMessage
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readRawBytes
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AccountRepositoryImpl(
    private val http: Http,
    private val apiHelper: ApiHelper,
) : AccountRepository {

    private suspend fun errorFrom(response: HttpResponse): Results<Nothing> {
        val error = apiHelper.safeApiCall(response.status) { response.body<ErrorDTO>() }
        return Results.error(error.data?.message ?: error.message ?: DEFAULT_ERROR)
    }

    override suspend fun saveSubscription(request: SaveSubscriptionRequest): Results<SubscriptionResponse> =
        try {
            val response = http.client.post("/api/mobile/subscriptions") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status != HttpStatusCode.OK) errorFrom(response)
            else apiHelper.safeApiCall(response.status) { response.body<SubscriptionResponse>() }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }

    override suspend fun getSubscription(): Results<SubscriptionResponse> =
        try {
            val response = http.client.get("/api/mobile/subscription")
            if (response.status != HttpStatusCode.OK) errorFrom(response)
            else apiHelper.safeApiCall(response.status) { response.body<SubscriptionResponse>() }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }

    override suspend fun downloadResultsPdf(uuid: String): Results<ByteArray> =
        try {
            val response = http.client.get("/api/mobile/results/$uuid/download")
            if (response.status != HttpStatusCode.OK) errorFrom(response)
            else Results.success(response.readRawBytes())
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }

    private companion object {
        const val DEFAULT_ERROR = "Something went wrong. Try again"
    }
}
