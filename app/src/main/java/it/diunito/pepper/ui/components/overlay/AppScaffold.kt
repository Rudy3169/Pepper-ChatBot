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
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween

// make isDark visible to all
val LocalIsDark = compositionLocalOf { false }

@Composable
fun AppScaffold(
    isDark: Boolean,
    isChatScreen: Boolean = false,
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
                    Surface(
                        color = if (isDark) Color(0xFF202C33) else Color.White,
                        shadowElevation = if (isDark) 0.dp else 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 64.dp)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                Crossfade(
                                    targetState = isChatScreen,
                                    animationSpec = tween(300),
                                    label = "header_crossfade"
                                ) { showChat ->
                                    if (showChat) {
                                        // Chat header: LLM dropdown + Pepper info
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            var expanded by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
                                            var selectedAI by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("Gemini") }

                                            val allAIs = listOf("Gemini", "ChatGPT", "Claude")
                                            var buttonHeightPx by androidx.compose.runtime.remember { androidx.compose.runtime.mutableIntStateOf(0) }

                                            // Fixed-width LLM selector button
                                            Box {
                                                Surface(
                                                    color = if (isDark) Color(0xFF2A3942) else Color.White,
                                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                                                    shadowElevation = 1.dp,
                                                    modifier = Modifier
                                                        .width(160.dp)
                                                        .onGloballyPositioned { buttonHeightPx = it.size.height }
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .clickable { expanded = !expanded }
                                                            .padding(horizontal = 14.dp, vertical = 8.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        androidx.compose.foundation.Image(
                                                            painter = androidx.compose.ui.res.painterResource(
                                                                id = when(selectedAI) {
                                                                    "ChatGPT" -> it.diunito.pepper.R.drawable.ic_chatgpt
                                                                    "Claude" -> it.diunito.pepper.R.drawable.ic_claude
                                                                    else -> it.diunito.pepper.R.drawable.ic_gemini
                                                                }
                                                            ),
                                                            contentDescription = selectedAI,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(8.dp))
                                                        androidx.compose.material3.Text(
                                                            text = selectedAI,
                                                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                                                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                                                                color = if (isDark) Color.White else Color.Black
                                                            ),
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                        androidx.compose.material3.Icon(
                                                            imageVector = Icons.Default.ArrowDropDown,
                                                            contentDescription = "Dropdown",
                                                            tint = if (isDark) Color(0xFF8696A0) else Color(0xFF667781),
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }
                                                }

                                                // Custom pill-shaped dropdown (Figma design)
                                                // Shows only NON-selected LLMs, rendered in a Popup
                                                if (expanded) {
                                                    val density = androidx.compose.ui.platform.LocalDensity.current
                                                    val offsetPx = with(density) { (buttonHeightPx + 4.dp.roundToPx()) }
                                                    androidx.compose.ui.window.Popup(
                                                        alignment = Alignment.TopStart,
                                                        offset = androidx.compose.ui.unit.IntOffset(0, offsetPx),
                                                        onDismissRequest = { expanded = false },
                                                        properties = androidx.compose.ui.window.PopupProperties(
                                                            focusable = true
                                                        )
                                                    ) {
                                                        Surface(
                                                            color = if (isDark) Color(0xFF2A3942) else Color.White,
                                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                                                            shadowElevation = 4.dp,
                                                            modifier = Modifier.width(160.dp)
                                                        ) {
                                                            Column(
                                                                modifier = Modifier.padding(vertical = 8.dp)
                                                            ) {
                                                                allAIs.filter { it != selectedAI }.forEach { aiName ->
                                                                    Row(
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .clickable {
                                                                                selectedAI = aiName
                                                                                expanded = false
                                                                            }
                                                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                                                        verticalAlignment = Alignment.CenterVertically
                                                                    ) {
                                                                        androidx.compose.foundation.Image(
                                                                            painter = androidx.compose.ui.res.painterResource(
                                                                                id = when(aiName) {
                                                                                    "ChatGPT" -> it.diunito.pepper.R.drawable.ic_chatgpt
                                                                                    "Claude" -> it.diunito.pepper.R.drawable.ic_claude
                                                                                    else -> it.diunito.pepper.R.drawable.ic_gemini
                                                                                }
                                                                            ),
                                                                            contentDescription = aiName,
                                                                            modifier = Modifier.size(20.dp)
                                                                        )
                                                                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(10.dp))
                                                                        androidx.compose.material3.Text(
                                                                            text = aiName,
                                                                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                                                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                                                                                color = if (isDark) Color.White else Color.Black
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            // Pepper info at fixed position
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(start = 20.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .clip(androidx.compose.foundation.shape.CircleShape)
                                                        .background(if (isDark) Color(0xFF2A3942) else Color(0xFFF0F2F5)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    androidx.compose.foundation.Image(
                                                        painter = androidx.compose.ui.res.painterResource(id = it.diunito.pepper.R.drawable.ic_pepper),
                                                        contentDescription = "Pepper",
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }
                                                androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    androidx.compose.material3.Text(
                                                        text = "Pepper",
                                                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                                        ),
                                                        color = if (isDark) Color.White else Color.Black
                                                    )
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        androidx.compose.material3.Text(
                                                            text = "Online",
                                                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                                            color = if (isDark) Color(0xFF8696A0) else Color(0xFF667781)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        // Welcome screen: empty spacer
                                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.fillMaxWidth())
                                    }
                                }
                            }

                            // Right: Theme & Language Toggles
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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
                        }
                    }
                },
                bottomBar = {
                    FooterLogos(
                        isDark = isDark,
                        // clickable logos just in case
                        onAirlabClick = onAirlabClick,
                        onDipInfoClick = onDipInfoClick,
                        onUnitoClick = onUnitoClick
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

