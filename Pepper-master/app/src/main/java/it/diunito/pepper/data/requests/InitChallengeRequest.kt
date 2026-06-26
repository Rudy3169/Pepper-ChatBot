package it.diunito.pepper.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class InitChallengeRequest(
    val recipeName: String,
    val conversationId: String
)
