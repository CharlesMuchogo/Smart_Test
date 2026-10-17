package com.charlesmuchogo.research.domain.viewmodels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.remote.AccountRepository
import com.charlesmuchogo.research.domain.dto.account.SaveSubscriptionRequest
import com.charlesmuchogo.research.domain.models.SnackBarItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: MultiplatformSettingsRepository,
    private val accountRepository: AccountRepository,
) : ViewModel(), PurchasesUpdatedListener {

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .enableAutoServiceReconnection()
        .build()


    private val _isBillingClientReady = MutableStateFlow(false)
    val isBillingClientReady = _isBillingClientReady.asStateFlow()

    private val _products = MutableStateFlow<List<ProductDetails>>(emptyList())
    val products = _products.asStateFlow()

    private val playActive = MutableStateFlow<Boolean?>(null)
    private val backendActive = MutableStateFlow(false)

    init {
        startBillingConnection()
        viewModelScope.launch {
            settingsRepository.getAccessToken()
                .map { !it.isNullOrBlank() }
                .distinctUntilChanged()
                .collect { signedIn ->
                    backendActive.value = signedIn && fetchBackendStatus()
                }
        }
        viewModelScope.launch {
            combine(playActive, backendActive) { play, backend ->
                if (play == null && !backend) null else play == true || backend
            }
                .filterNotNull()
                .distinctUntilChanged()
                .collect { settingsRepository.saveSubscriptionStatus(it) }
        }
    }

    private suspend fun fetchBackendStatus(): Boolean {
        val result = accountRepository.getSubscription()
        return result.data?.subscribed == true
    }

    private fun startBillingConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _isBillingClientReady.value = true
                    queryProducts()
                    restorePurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                println("_isBillingClientReady disconnected")
                _isBillingClientReady.value = false
            }
        })
    }

    private fun queryProducts() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("com.charles.research.monthly_sub")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("com.charles.research.yearly_sub")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                println("products are -> ${productDetailsList.productDetailsList}")
                _products.value = productDetailsList.productDetailsList
            }
        }
    }


    private fun restorePurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val active = purchases.filter { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                }
                playActive.value = active.isNotEmpty()
                active.forEach { syncWithBackend(it) }
            }
        }
    }

    fun buySubscription(activity: Activity, productId: String) {
        println("productId $productId, ${products.value}")

        val productDetails = _products.value.find { it.productId == productId }
        if (productDetails == null) {
            viewModelScope.launch {
                SnackBarViewModel.sendEvent(SnackBarItem("Product not found", isError = true))
            }
            return
        }

        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: ""

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            viewModelScope.launch {
                SnackBarViewModel.sendEvent(SnackBarItem("Purchase cancelled"))
            }
        } else {
            viewModelScope.launch {
                SnackBarViewModel.sendEvent(SnackBarItem("Error: ${billingResult.debugMessage}"))
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            playActive.value = true

            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    val acknowledged = billingResult.responseCode == BillingClient.BillingResponseCode.OK
                    syncWithBackend(purchase, acknowledged = acknowledged)
                    if (acknowledged) {
                        viewModelScope.launch {
                            SnackBarViewModel.sendEvent(SnackBarItem("Subscription successful!"))
                        }
                    }
                }
            } else {
                syncWithBackend(purchase)
            }
        }
    }

    private fun syncWithBackend(purchase: Purchase, acknowledged: Boolean = purchase.isAcknowledged) {
        val productId = purchase.products.firstOrNull() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val token = settingsRepository.getAccessToken().firstOrNull()
            if (token.isNullOrBlank()) return@launch
            val result = accountRepository.saveSubscription(
                SaveSubscriptionRequest(
                    productId = productId,
                    purchaseToken = purchase.purchaseToken,
                    orderId = purchase.orderId.orEmpty(),
                    purchaseTime = purchase.purchaseTime,
                    autoRenewing = purchase.isAutoRenewing,
                    acknowledged = acknowledged,
                )
            )
            result.message?.let { println("saving subscription failed: $it") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        billingClient.endConnection()
    }
}
