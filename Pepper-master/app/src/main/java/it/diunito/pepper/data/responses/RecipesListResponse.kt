package it.diunito.pepper.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class RecipesListResponse(
    val recipes: List<String>
)
