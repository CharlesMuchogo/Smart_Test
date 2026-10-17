package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.domain.dto.account.SaveSubscriptionRequest
import com.charlesmuchogo.research.domain.dto.account.SubscriptionResponse
import com.charlesmuchogo.research.presentation.utils.Results

interface AccountRepository {
    suspend fun saveSubscription(request: SaveSubscriptionRequest): Results<SubscriptionResponse>

    suspend fun getSubscription(): Results<SubscriptionResponse>

    suspend fun downloadResultsPdf(uuid: String): Results<ByteArray>
}
