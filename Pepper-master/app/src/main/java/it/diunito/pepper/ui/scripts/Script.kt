package it.diunito.pepper.ui.scripts

/**
 * Example of retrieving dynamic text labels based on the selected language */

/*

// recall language labels from LanguageHandler
val labels = LocalLanguageHandler.current.labels.collectAsState().value

val title = labels.chatTitle
val subtitle = labels.chatMessage

*/


class Script {
    companion object {
        val chatTitle: String = "La nostra conversazione"
        val chatMessage: String = "Chiedimi qualcosa..."
        val chatInputPlaceholder: String = "Scrivi un messaggio..."
        val engageTitle: String = "Come Funziona?"
        val engageInstruction: String =
            "Per dirmi qualcosa, premi il pulsante \"Parla\", oppure usa la chat per scrivermi un messaggio."
        val engageWarning: String =
            "Prima di andar via, ricordati di salutarmi premento il pulsante \" Alla prossima \" qui sotto, per prepararmi al prossimo incontro"
    }
}