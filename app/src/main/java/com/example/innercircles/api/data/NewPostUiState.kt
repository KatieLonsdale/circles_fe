package com.example.innercircles.api.data

import com.example.innercircles.ui.ContentViewModel

data class NewPostUiState(
    val caption: String = "",
    val contents: ContentViewModel? = null,
    val circleIds: List<String> = listOf(),
)
