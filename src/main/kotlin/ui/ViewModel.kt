package com.tkhskt.ankideckgenerator.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive

open class ViewModel {
    protected val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob())
        get() = if (field.isActive) field else CoroutineScope(SupervisorJob())
}
