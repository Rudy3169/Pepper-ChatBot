package it.diunito.pepper.data.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.diunito.pepper.data.requests.LightsRequest
import it.diunito.pepper.data.requests.SayRequest
import it.diunito.pepper.data.responses.ResultResponse

class HeadApiService(private val client: HttpClient, private val headHost: String) {

    val timeoutMillis = 2000L

    suspend fun postSay(request: SayRequest) : ResultResponse{
        try {
            val response: HttpResponse = client.post("$headHost/say"){
                contentType(ContentType.Application.Json)
                setBody(request)
                timeout {
                    requestTimeoutMillis = timeoutMillis
                }
            }
            return if (response.status.value in 200..299){
                response.body<ResultResponse>()
            } else ResultResponse(result=-1)
        } catch (e: Exception){
            return ResultResponse(10000)
        }
    }

    suspend fun getStopAll(): ResultResponse{
        try {
            val response: HttpResponse = client.get("$headHost/stop-all"){
                timeout {
                    requestTimeoutMillis = timeoutMillis
                }
            }
            return if (response.status.value in 200.. 299){
                response.body<ResultResponse>()
            } else ResultResponse(-1)
        } catch (e: Exception){
            return ResultResponse(10001)
        }
    }

    suspend fun postLights(request: LightsRequest) : ResultResponse{
        try {
            val response: HttpResponse = client.post("$headHost/lights"){
                contentType(ContentType.Application.Json)
                setBody(request)
                timeout {
                    requestTimeoutMillis = timeoutMillis
                }
            }
            return if (response.status.value in 200..299){
                response.body<ResultResponse>()
            } else ResultResponse(result=-1)
        } catch (e: Exception){
            return ResultResponse(10002)
        }
    }

}