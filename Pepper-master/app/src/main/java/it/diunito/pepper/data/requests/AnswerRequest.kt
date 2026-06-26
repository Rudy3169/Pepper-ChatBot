package it.diunito.pepper.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AnswerRequest(
    val content: String,
    val conversationId: String
)
