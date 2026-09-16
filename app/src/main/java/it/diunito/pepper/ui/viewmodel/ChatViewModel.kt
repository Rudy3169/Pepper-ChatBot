package it.diunito.pepper.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.diunito.pepper.BuildConfig
import it.diunito.pepper.data.KtorClient
import it.diunito.pepper.data.LlmModel
import it.diunito.pepper.data.requests.LightsRequest
import it.diunito.pepper.data.requests.SayRequest
import it.diunito.pepper.data.services.GatewayApiService
import it.diunito.pepper.data.services.HeadApiService
import it.diunito.pepper.data.services.LlmApiService
import it.diunito.pepper.data.services.LlmHttpException
import io.ktor.client.plugins.HttpRequestTimeoutException
import it.diunito.pepper.ui.components.chat.ChatMessage
import it.diunito.pepper.ui.components.chat.Sender
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val llmApi = LlmApiService(
        KtorClient.client,
        BuildConfig.LLM_API_ENDPOINT,
        BuildConfig.LLM_API_KEY
    )

    private val _chat = MutableLiveData<List<ChatMessage>>(emptyList())
    val chat: LiveData<List<ChatMessage>> = _chat
    private var _conversationId = UUID.randomUUID().toString()

    // LLM model selection
    private val _selectedModel = MutableStateFlow(LlmModel.GPT_OSS)
    val selectedModel: StateFlow<LlmModel> = _selectedModel.asStateFlow()

    fun selectModel(model: LlmModel) {
        if (model == _selectedModel.value) return
        _selectedModel.value = model
        // Reset conversation context when switching models
        llmApi.resetConversation()
        _chat.value = emptyList()
        _conversationId = UUID.randomUUID().toString()
        Log.d("ChatViewModel", "Switched to model: ${model.displayName}")
    }

    // Track the active dialogue coroutine so it can be cancelled
    private var _dialogueJob: Job? = null

    // Exposed to the UI: true while a dialogue turn is in progress
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()
    fun flush(updateUserTyping: (typing: Boolean) -> Unit,
              updatePepperTyping: (typing: Boolean) -> Unit,){
        // TODO: stop conversation when pressed.
        _chat.value = emptyList()
        _conversationId = UUID.randomUUID().toString()
        llmApi.resetConversation()
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

    fun cancelDialogueTurn(
        updateUserTyping: (typing: Boolean) -> Unit,
        updatePepperTyping: (typing: Boolean) -> Unit
    ) {
        _dialogueJob?.cancel()
        _dialogueJob = null
        _isProcessing.value = false
        updateUserTyping(false)
        updatePepperTyping(false)
        viewModelScope.launch {
            headApi.getStopAll()
        }
    }

    fun dialogueTurn(
        updateUserTyping: (typing: Boolean) -> Unit,
        updatePepperTyping: (typing: Boolean) -> Unit,
        content: String? = null,
        errorMessage: String = "Scusa, in questo momento non riesco a pensare bene. Possiamo riprovare tra poco?"
    ){
        _dialogueJob?.cancel()
        _isProcessing.value = true
        _dialogueJob = viewModelScope.launch {
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
                    val messageText = gatewayApi.getListen()

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

                // Call the LLM endpoint directly to get the response
                val answerText = llmApi.chat(message, _selectedModel.value.modelId)

                val pepperMessage = ChatMessage(
                    id = _chat.value?.size ?: 0,
                    sender = Sender.PEPPER,
                    text = answerText
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
                        message = answerText
                    )
                )
                headApi.postLights(
                    LightsRequest(
                        setLights = false,
                        groups = arrayListOf("ChestLeds")
                    )
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: TimeoutCancellationException) {
                Log.e("ERROR", "Timeout", e)
                handleDialogueError(updateUserTyping, updatePepperTyping, "Ci sto mettendo troppo tempo a pensare a causa del server sovraccarico. Riproviamo?")
            } catch (e: HttpRequestTimeoutException) {
                Log.e("ERROR", "HTTP Timeout", e)
                handleDialogueError(updateUserTyping, updatePepperTyping, "Ci sto mettendo troppo tempo a pensare a causa del server sovraccarico. Riproviamo?")
            } catch (e: LlmHttpException) {
                Log.e("ERROR", "LLM HTTP Error: ${e.statusCode}", e)
                val msg = if (e.statusCode == 403) {
                    "Il server AI ha restituito un errore di permessi o non trova il modello (Errore 403)."
                } else {
                    "Il server AI ha restituito un errore (HTTP ${e.statusCode})."
                }
                handleDialogueError(updateUserTyping, updatePepperTyping, msg)
            } catch (e: Exception){
                Log.e("ERROR", "Error on listen and speech",e)
                val debugMsg = "Errore di rete o di sistema: ${e.javaClass.simpleName} - ${e.localizedMessage}. Verifica la connessione internet dell'emulatore."
                handleDialogueError(updateUserTyping, updatePepperTyping, debugMsg)
            } finally {
                _isProcessing.value = false
                _dialogueJob = null
            }
        }
    }

    private fun handleDialogueError(
        updateUserTyping: (typing: Boolean) -> Unit,
        updatePepperTyping: (typing: Boolean) -> Unit,
        message: String
    ) {
        updateUserTyping(false)
        updatePepperTyping(false)
        val pepperErrorMessage = ChatMessage(
            id = _chat.value?.size ?: 0,
            sender = Sender.PEPPER,
            text = message
        )
        _chat.value = _chat.value.orEmpty() + pepperErrorMessage
    }
}