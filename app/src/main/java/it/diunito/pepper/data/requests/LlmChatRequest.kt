package it.diunito.pepper.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class LlmMessage(
    val role: String,
    val content: String
)

@Serializable
data class LlmChatRequest(
    val model: String,
    val messages: List<LlmMessage>,
    val temperature: Double = 0.7,
    val stream: Boolean = false
)
