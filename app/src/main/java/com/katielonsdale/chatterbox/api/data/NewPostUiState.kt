package com.katielonsdale.chatterbox.api.data

import com.katielonsdale.chatterbox.ui.ContentViewModel

data class NewPostUiState(
    val caption: String = "",
    val contents: ContentViewModel? = null,
    val circleIds: List<String> = listOf(),
)
