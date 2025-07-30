package com.test.snotes.dataModel

data class Note(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val reminderTime: String = "",
    var isAlarmOn: Boolean = false,
    val userId: String = ""
)