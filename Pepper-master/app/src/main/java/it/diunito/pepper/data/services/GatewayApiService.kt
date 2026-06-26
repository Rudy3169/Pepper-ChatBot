package it.diunito.pepper.data.services

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.diunito.pepper.data.requests.AnswerRequest
import it.diunito.pepper.data.requests.InitChallengeRequest
import it.diunito.pepper.data.requests.SayStepRequest
import it.diunito.pepper.data.responses.AnswerResponse
import it.diunito.pepper.data.responses.CheckResponse
import it.diunito.pepper.data.responses.InitChallengeResponse
import it.diunito.pepper.data.responses.IntroResponse
import it.diunito.pepper.data.responses.ListenResponse
import it.diunito.pepper.data.responses.RecipesListResponse
import it.diunito.pepper.data.responses.SayStepResponse

class GatewayApiService(private val client: HttpClient, private val gatewayHost: String) {


    val timeoutMillis = 5000L

    suspend fun getListen(): ListenResponse {
        val response : HttpResponse = client.get("$gatewayHost/dialogue/listen"){
            timeout {
                    requestTimeoutMillis =  timeoutMillis * 30
            }
        }
        if (response.status.value in 200..299){
            Log.d("DEBUG", response.body())
            return response.body<ListenResponse>()
        }
        else {
            return ListenResponse(transcription = "Errore in fase di trascrizione.")
        }
    }

    suspend fun postAnswer(request: AnswerRequest): AnswerResponse{
        val response : HttpResponse = client.post("$gatewayHost/dialogue/answer") {
            contentType(ContentType.Application.Json)
            setBody(request)
            timeout {
                requestTimeoutMillis =  timeoutMillis * 30
            }
        }

        if (response.status.value in 200..299){
            Log.d("DEBUG", response.body())
            return response.body<AnswerResponse>()
        }
        else {
            return AnswerResponse(output = "Sono stanco, non credo di aver capito, puoi ripetere?")
        }
    }

    suspend fun getCheck(): CheckResponse{
        val response : HttpResponse = client.get("$gatewayHost/dialogue/check"){
            timeout {
                requestTimeoutMillis =  timeoutMillis
            }
        }

        if (response.status.value in 200..299){
            Log.d("DEBUG", response.body())
            return response.body<CheckResponse>()
        }
        else {
            return CheckResponse(response = 0)
        }
    }

    suspend fun getRecipesList(): RecipesListResponse{
        val response : HttpResponse = client.get("$gatewayHost/challenge/recipes-list")
        if (response.status.value in 200..299){
            Log.d("DEBUG", response.body())
            return response.body<RecipesListResponse>()
        }
        else {
            return RecipesListResponse(recipes = emptyList())
        }
    }

    suspend fun postInitChallenge(request: InitChallengeRequest) : InitChallengeResponse {
        val response: HttpResponse = client.post("$gatewayHost/challenge/init-challenge") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (response.status.value in 200..299){
            Log.d("DEBUG", response.body())
            return response.body<InitChallengeResponse>()
        }
        else {
            return InitChallengeResponse(
                name = "",
                ingredients = emptyList(),
                steps = emptyList(),
                intro = "",
                ingredientsDescription = ""
            )
        }
    }

    suspend fun postGetIntro(): IntroResponse{
        val response : HttpResponse = client.get("$gatewayHost/challenge/intro")

        if (response.status.value in 200..299){
            Log.d("DEBUG", response.body())
            return response.body<IntroResponse>()
        }
        else {
            return IntroResponse(recipeIntro = "")
        }
    }

    suspend fun getFunFact(): AnswerResponse{
        val response : HttpResponse = client.get("$gatewayHost/challenge/fun-fact")

        if (response.status.value in 200..299){
            Log.d("DEBUG", response.body())
            return response.body<AnswerResponse>()
        }
        else {
            return AnswerResponse(output = "")
        }
    }

    suspend fun postSayStep(request: SayStepRequest): SayStepResponse{
        val response : HttpResponse = client.post("$gatewayHost/challenge/say-step") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (response.status.value in 200..299){
            Log.d("DEBUG", response.body())
            return response.body<SayStepResponse>()
        }
        else {
            return SayStepResponse(stepText = "")
        }
    }

}