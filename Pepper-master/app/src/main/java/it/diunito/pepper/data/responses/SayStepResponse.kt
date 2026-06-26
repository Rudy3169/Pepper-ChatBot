package it.diunito.pepper.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class SayStepResponse(
    val stepText: String
)
