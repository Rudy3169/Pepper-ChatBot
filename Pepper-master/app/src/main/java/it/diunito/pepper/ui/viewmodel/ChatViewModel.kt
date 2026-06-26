package it.diunito.pepper.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.diunito.pepper.BuildConfig
import it.diunito.pepper.data.KtorClient
import it.diunito.pepper.data.requests.AnswerRequest
import it.diunito.pepper.data.requests.LightsRequest
import it.diunito.pepper.data.requests.SayRequest
import it.diunito.pepper.data.responses.AnswerResponse
import it.diunito.pepper.data.responses.ListenResponse
import it.diunito.pepper.data.services.GatewayApiService
import it.diunito.pepper.data.services.HeadApiService
import it.diunito.pepper.ui.components.chat.ChatMessage
import it.diunito.pepper.ui.components.chat.Sender
import kotlinx.coroutines.launch
import java.util.UUID


class ChatViewModel : ViewModel() {

    private val gatewayApi = GatewayApiService(
        KtorClient.client,
        BuildConfig.GATEWAY_API_HOST
    )

    private val headApi = HeadApiService(
        KtorClient.client,
        BuildConfig.HEAD_API_HOST
    )

    private val _chat = MutableLiveData<List<ChatMessage>>(emptyList())
    val chat: LiveData<List<ChatMessage>> = _chat
    private var _conversationId = UUID.randomUUID().toString()
    fun flush(updateUserTyping: (typing: Boolean) -> Unit,
              updatePepperTyping: (typing: Boolean) -> Unit,){
        // TODO: stop conversation when pressed.
        _chat.value = emptyList()
        _conversationId = UUID.randomUUID().toString()
        updateUserTyping(false)
        updatePepperTyping(false)
        viewModelScope.launch {
            headApi.getStopAll()
        }
    }

    fun stopSpeech(){
        viewModelScope.launch {
            headApi.getStopAll()
        }
    }

    fun dialogueTurn(
        updateUserTyping: (typing: Boolean) -> Unit,
        updatePepperTyping: (typing: Boolean) -> Unit,
        content: String? = null
    ){
        viewModelScope.launch {
            try {
                var message: String = content ?: ""
                if(content == null) { // the user chose to speak
                    // LISTENING PHASE
                    updateUserTyping(true)

                    headApi.postLights(
                        LightsRequest(
                            setLights = true,
                            groups = arrayListOf("EarLeds")
                        )
                    )
                    val messageText: ListenResponse = gatewayApi.getListen()

                    // print chat message
                    val userMessage = ChatMessage(
                        id = _chat.value?.size ?: 0,
                        sender = Sender.USER,
                        text = messageText.transcription
                    )
                    _chat.value = _chat.value.orEmpty() + userMessage

                    // LLM RESPONSE PHASE
                    headApi.postLights(
                        LightsRequest(
                            setLights = false,
                            groups = arrayListOf("EarLeds")
                        )
                    )
                    message = messageText.transcription
                }
                // else the user wrote a message
                /* From here on it's in common with both choices */

                if (
                    content != null
                ) {
                    // print chat message
                    val userMessage = ChatMessage(
                        id = _chat.value?.size ?: 0,
                        sender = Sender.USER,
                        text = message)
                    _chat.value = _chat.value.orEmpty() + userMessage
                }

                updateUserTyping(false)
                updatePepperTyping(true)
                val answerText : AnswerResponse = gatewayApi.postAnswer(
                    AnswerRequest(message, _conversationId)
                )
                val pepperMessage = ChatMessage(
                    id = _chat.value?.size ?: 0,
                    sender = Sender.PEPPER,
                    text = answerText.output
                )
                _chat.value = _chat.value.orEmpty() + pepperMessage
                updatePepperTyping(false)

                headApi.postLights(
                    LightsRequest(
                        setLights = true,
                        groups = arrayListOf("ChestLeds")
                    )
                )
                headApi.postSay(
                    SayRequest(
                        message = answerText.output
                    )
                )
                headApi.postLights(
                    LightsRequest(
                        setLights = false,
                        groups = arrayListOf("ChestLeds")
                    )
                )
            } catch (e: Exception){
                Log.e("ERROR", "Error on listen and speech",e)
                updateUserTyping(false)
                updateUserTyping(false)
            }
        }
    }
}