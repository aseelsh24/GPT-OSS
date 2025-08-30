package com.example.gptoss

data class Message(
    val text: String,
    val viewType: Int
) {
    companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_AI = 2
    }
}
