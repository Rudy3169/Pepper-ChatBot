package it.diunito.pepper.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class LightsRequest(
    val setLights: Boolean,
    val groups: List<String>
)
