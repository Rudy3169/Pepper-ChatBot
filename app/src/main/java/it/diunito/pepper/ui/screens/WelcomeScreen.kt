package it.diunito.pepper.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.diunito.pepper.R
import it.diunito.pepper.ui.components.buttons.AppButton
import it.diunito.pepper.ui.scripts.LocalLanguageHandler
import it.diunito.pepper.ui.theme.ClientIcons
import it.diunito.pepper.ui.theme.UNITOred

@Composable
fun WelcomeScreen(
    onStartChat: () -> Unit
) {
    val labels = LocalLanguageHandler.current.labels.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pepper),
                contentDescription = "Pepper",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = labels.welcomeTitle,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = UNITOred
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = labels.welcomeSubtitle,
                style = MaterialTheme.typography.headlineSmall.copy(
                    lineHeight = 32.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 48.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            AppButton(
                label = labels.startChatting,
                onClick = onStartChat,
                myIcon = ClientIcons.conversation(),
                width = 300.dp,
                height = 80.dp
            )
        }
    }
}
