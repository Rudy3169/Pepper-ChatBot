package it.diunito.pepper.data.responses

import it.diunito.pepper.data.requests.LlmMessage
import kotlinx.serialization.Serializable

@Serializable
data class LlmChoice(
    val message: LlmMessage
)

@Serializable
data class LlmChatResponse(
    val choices: List<LlmChoice>
)
