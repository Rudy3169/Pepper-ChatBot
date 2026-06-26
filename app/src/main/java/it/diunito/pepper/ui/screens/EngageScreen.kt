package it.diunito.pepper.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.diunito.pepper.ui.scripts.AppLanguage
import it.diunito.pepper.ui.scripts.LanguageHandler
import it.diunito.pepper.ui.scripts.LocalLanguageHandler
import it.diunito.pepper.ui.scripts.loadLanguages
import it.diunito.pepper.ui.theme.ClientIcons
import it.diunito.pepper.ui.theme.UNITOred
import it.diunito.pepper.ui.components.buttons.AppButton
import it.diunito.pepper.ui.components.buttons.AppButtonColorsRed
import it.diunito.pepper.ui.components.buttons.ButtonFixedWidth
import it.diunito.pepper.ui.components.chat.ChatBox
import it.diunito.pepper.ui.components.chat.ChatHeader
import it.diunito.pepper.ui.components.chat.ChatInputBar
import it.diunito.pepper.ui.components.chat.ChatMessage
import it.diunito.pepper.ui.components.overlay.LocalIsDark
import it.diunito.pepper.ui.viewmodel.ChatViewModel
import it.diunito.pepper.ui.scripts.LocalLanguageHandler as lang

@Composable
fun EngageScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {
    // load chat messages
    val messages: List<ChatMessage> by viewModel.chat.observeAsState(emptyList())

    var isUserTyping : Boolean by remember { mutableStateOf(false) }
    var isPepperTyping : Boolean by remember { mutableStateOf(false) }

    fun updateIsUserTyping(value: Boolean) {
        isUserTyping = value
    }

    fun updateIsPepperTyping(value: Boolean) {
        isPepperTyping = value
    }

    // load available languages
    val labels = lang.current.labels.collectAsState().value

    // input bar
    var input by rememberSaveable { mutableStateOf("") }
    val inputFocus = remember { FocusRequester() }
//    val keyboard = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        // left screen half for chat
        Column(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
                .padding(12.dp, 12.dp, 24.dp, 12.dp)
        ) {
            // Empty container of the chat
            Surface(
                tonalElevation = 1.dp,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // this will contain the chat
                Column(Modifier.fillMaxSize()) {
                    ChatHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                    HorizontalDivider(
                        Modifier,
                        DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    ChatBox(
                        showUserTyping = isUserTyping,
                        showPepperTyping = isPepperTyping,
                        messages = messages,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .border(
                                width = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            )
                    )
                    ChatInputBar(
                        value = input,
                        onValueChange = { input = it },
                        onSend = {
                            val text = input.trim()
                            if (text.isNotEmpty()) {
                                viewModel.stopSpeech()
                                viewModel.dialogueTurn(
                                    updateUserTyping = { isUserTyping = it },
                                    updatePepperTyping = { isPepperTyping = it },
                                    content = text,
                                )
                                input = ""
                            }
                        },
                        focusRequester = inputFocus,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Right half for feedbacks and controls
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(0.dp, 12.dp, 32.dp, 12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                labels.engageTitle,
                style = MaterialTheme.typography.titleLarge
                    .copy(color = UNITOred, textAlign = TextAlign.Left),
            )

            HorizontalDivider(
                Modifier
                    .padding(top = 4.dp, bottom = 12.dp)
                    .width(120.dp)
                    .height(4.dp)
                    .background(UNITOred, RoundedCornerShape(8.dp)),
                color = UNITOred
            )

            Text(labels.engageInstruction,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
            )
            AppButton(
                label = labels.talk,
                modifier = Modifier
                    .padding(6.dp)
                    .width(ButtonFixedWidth)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    viewModel.stopSpeech()
                    viewModel.dialogueTurn(
                        updateUserTyping = { isUserTyping = it },
                        updatePepperTyping = { isPepperTyping = it },
                    )
                },
                myIcon = ClientIcons.mic(),
                enabled = !isUserTyping && !isPepperTyping
            )

            Text(labels.engageWarning,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
            )

            AppButton(
                label = labels.goodbye,
                modifier = Modifier
                    .padding(6.dp)
                    .width(ButtonFixedWidth)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    viewModel.stopSpeech()
                    viewModel.flush(
                        updateUserTyping = { isUserTyping = it },
                        updatePepperTyping = { isPepperTyping = it },
                    )
                },
                colors = AppButtonColorsRed(),
                icon = Icons.Outlined.Refresh
            )
        }
    }
}
