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
import it.diunito.pepper.data.responses.AnswerResponse
import it.diunito.pepper.data.responses.ListenResponse

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

}