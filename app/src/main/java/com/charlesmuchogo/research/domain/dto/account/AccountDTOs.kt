package com.charlesmuchogo.research.domain.dto.account

import kotlinx.serialization.Serializable

@Serializable
data class SaveSubscriptionRequest(
    val productId: String,
    val purchaseToken: String,
    val orderId: String = "",
    val purchaseTime: Long = 0,
    val autoRenewing: Boolean = false,
    val acknowledged: Boolean = false,
)

@Serializable
data class SubscriptionRecord(
    val id: Long = 0,
    val productId: String = "",
    val orderId: String = "",
    val status: String = "",
    val autoRenewing: Boolean = false,
    val verified: Boolean = false,
    val expiresAt: String? = null,
)

@Serializable
data class SubscriptionResponse(
    val message: String = "",
    val subscribed: Boolean = false,
    val subscription: SubscriptionRecord? = null,
)
