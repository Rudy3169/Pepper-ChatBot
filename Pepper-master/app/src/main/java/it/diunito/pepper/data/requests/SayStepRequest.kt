package it.diunito.pepper.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class SayStepRequest(
    val stepNumber: Int
)
