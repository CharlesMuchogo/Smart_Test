package com.charlesmuchogo.research.navigation

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface DeepLink {
    data class Tab(val index: Int) : DeepLink
    data class Screen(val route: Any, val tab: Int? = null) : DeepLink
}

object DeepLinks {
    const val LINK_EXTRA = "link"
    const val CONVERSATION_ID_EXTRA = "conversationId"
    const val HOST = "smarttest.muchogoc.com"
    private const val APP_PREFIX = "app"

    const val HOME_TAB = 0
    const val LEARN_TAB = 1
    const val MESSAGES_TAB = 2
    const val PROFILE_TAB = 3

    fun link(vararg segments: Any): String =
        (listOf("https://$HOST", APP_PREFIX) + segments.map { it.toString() }).joinToString("/")

    fun parse(uri: Uri): DeepLink? {
        if (uri.host != HOST) return null
        val segments = uri.pathSegments.orEmpty()
        if (segments.firstOrNull() != APP_PREFIX) return null
        val path = segments.drop(1)
        val id = path.getOrNull(1)

        return when (path.firstOrNull()) {
            null, "home" -> DeepLink.Tab(HOME_TAB)
            "learn" -> DeepLink.Tab(LEARN_TAB)
            "messages" -> DeepLink.Tab(MESSAGES_TAB)
            "profile" -> DeepLink.Tab(PROFILE_TAB)
            "articles" -> id?.let { DeepLink.Screen(ArticleDetailsPage(it), LEARN_TAB) } ?: DeepLink.Tab(LEARN_TAB)
            "conversations" -> id?.toLongOrNull()?.let { DeepLink.Screen(ConversationPage(it), MESSAGES_TAB) }
                ?: DeepLink.Tab(MESSAGES_TAB)
            "results" -> id?.toLongOrNull()?.let { DeepLink.Screen(TestResultsPage(it)) } ?: DeepLink.Screen(HistoryPage)
            "test" -> DeepLink.Screen(TestPage)
            "doctors" -> DeepLink.Screen(DoctorsPage, MESSAGES_TAB)
            "community" -> DeepLink.Screen(CommunityPage, MESSAGES_TAB)
            "clinics" -> DeepLink.Screen(ClinicsPage)
            "assistant" -> DeepLink.Screen(ChatPage)
            "subscribe" -> DeepLink.Screen(SubscribePage)
            "blocked" -> DeepLink.Screen(BlockedUsersPage, PROFILE_TAB)
            else -> DeepLink.Tab(HOME_TAB)
        }
    }

    fun fromIntent(intent: Intent?): DeepLink? {
        intent ?: return null
        intent.getStringExtra(LINK_EXTRA)?.let { value ->
            parse(value.toUri())?.let { return it }
        }
        intent.getStringExtra(CONVERSATION_ID_EXTRA)?.toLongOrNull()?.let {
            return DeepLink.Screen(ConversationPage(it), MESSAGES_TAB)
        }
        return intent.data?.let(::parse)
    }
}

object PendingNavigation {
    private val _target = MutableStateFlow<DeepLink?>(null)
    val target: StateFlow<DeepLink?> = _target.asStateFlow()

    fun consume(intent: Intent?) {
        val link = DeepLinks.fromIntent(intent) ?: return
        intent?.removeExtra(DeepLinks.LINK_EXTRA)
        intent?.removeExtra(DeepLinks.CONVERSATION_ID_EXTRA)
        intent?.data = null
        _target.value = link
    }

    fun clear() {
        _target.value = null
    }
}
