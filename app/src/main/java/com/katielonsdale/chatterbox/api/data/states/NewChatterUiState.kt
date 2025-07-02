package com.katielonsdale.chatterbox.api.data.states

data class NewChatterUiState (
    val name: String = "",
    val description: String = "",
    val memberIds: MutableList<String> = mutableListOf(""),
)
