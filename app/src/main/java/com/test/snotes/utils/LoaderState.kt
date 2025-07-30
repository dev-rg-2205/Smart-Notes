package com.test.snotes.utils

import androidx.compose.runtime.mutableStateOf

object LoaderState {
    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

    fun show() {
        _isLoading.value = true
    }

    fun hide() {
        _isLoading.value = false
    }
}
