package com.test.snotes.navigator

import kotlinx.serialization.Serializable

sealed class Router {

    @Serializable
    object SplashScreen

    @Serializable
    object SignInScreen

    @Serializable
    object NotesScreen

    @Serializable
    data class AddEditNoteScreen(
        var id: String = "",
        var title: String = "",
        var description: String = "",
        var reminderTime: String = "",
        var userId: String = "",
        var isAlarmOn: Boolean = false,
    )





}