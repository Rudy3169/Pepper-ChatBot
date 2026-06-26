package it.diunito.pepper.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class SayRequest(
    val message: String
)