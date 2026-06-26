package it.diunito.pepper.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class CheckResponse(
    val response: Int
)
