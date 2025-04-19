
package com.charlesmuchogo.research.domain.viewmodels

import com.charlesmuchogo.research.domain.models.SnackBarItem
import io.ktor.events.Events
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


object SnackBarViewModel {
 private val _events = Channel<SnackBarItem>()
    val events = _events.receiveAsFlow()


    suspend fun sendEvent(event: SnackBarItem){
        _events.send(event)
    }

}
