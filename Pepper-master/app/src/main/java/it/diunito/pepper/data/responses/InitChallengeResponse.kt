package it.diunito.pepper.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class InitChallengeResponse(
    val name: String,
    val intro: String,
    val ingredients: List<Map<String, String>>,
    val steps: List<String>,
    val ingredientsDescription: String
)
