package it.diunito.pepper.ui.components.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.diunito.pepper.ui.scripts.AppLanguage
import it.diunito.pepper.ui.scripts.LanguageHandler
import it.diunito.pepper.ui.scripts.LocalLanguageHandler

// make isDark visible to all
val LocalIsDark = compositionLocalOf { false }

@Composable
fun AppScaffold(
    isDark: Boolean,
    languageHandler: LanguageHandler,
    onToggleTheme: () -> Unit,
    onResetToSystem: (() -> Unit)? = null,
    onAirlabClick: () -> Unit,
    onDipInfoClick: () -> Unit,
    onUnitoClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    CompositionLocalProvider(LocalLanguageHandler provides languageHandler) {
        val bgGradient = if (isDark) {
            Brush.verticalGradient(listOf(Color(0xFF202C33), Color(0xFF111B21)))
        } else {
            Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF3F4F6)))
        }
        Box(modifier = Modifier.fillMaxSize().background(bgGradient)) {
            Scaffold(
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, end = 60.dp)
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        ThemeToggle(
                            isDark = isDark,
                            onToggle = onToggleTheme,
                            onResetToSystem = onResetToSystem
                        )
                        val currentLang by languageHandler.currentLanguage.collectAsState()

                        LanguageSwitch(
                            language = currentLang.code,
                            onSwitchLanguage = {
                                val next = if (currentLang == AppLanguage.IT) AppLanguage.EN else AppLanguage.IT
                                languageHandler.setLanguage(next)
                            }
                        )
                    }
                },
                bottomBar = {
                    FooterLogos(
                        isDark = isDark,
                        // clickable logos just in case
                        onAirlabClick = onAirlabClick,
                        onDipInfoClick = onDipInfoClick,
                        onUnitoClick = onUnitoClick,
                        height = 44.dp
                    )
                }
            ) { innerPadding ->
                CompositionLocalProvider(LocalIsDark provides isDark) {
                    content(innerPadding)
                }
            }
        }
    }
}

