package it.diunito.pepper.ui.components.chat

data class ChatMessage(
    val id: Int,
    val text: String,
    val sender: Sender
)

enum class Sender { USER, PEPPER }

enum class BubbleSide { LEFT, RIGHT }

