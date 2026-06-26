package com.luisvertiz.nutriscan.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {

    private val _events = MutableSharedFlow<GlobalEvent>()
    val event: SharedFlow<GlobalEvent> = _events.asSharedFlow()

    suspend fun emit(event: GlobalEvent) {
        _events.emit(event)
    }

}

sealed class GlobalEvent {
    object RefreshHome : GlobalEvent()
}