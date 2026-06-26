package it.diunito.pepper.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.theme.ClientIcons
import it.diunito.pepper.ui.components.buttons.NavButton


@Composable
fun MainScreen(
    onGoEngage: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pick the modality", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                24.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            NavButton(
                label = "Chat",
                onClick = onGoEngage,
                myIcon = ClientIcons.conversation()
            )
            // Possiamo aggiungere altri pulsanti qui in futuro
        }
    }
}
