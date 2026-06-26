package it.diunito.pepper

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import it.diunito.pepper.ui.components.overlay.AppScaffold
import it.diunito.pepper.ui.navigation.AppNavGraph
import it.diunito.pepper.ui.scripts.AppLanguage
import it.diunito.pepper.ui.scripts.LanguageHandler
import it.diunito.pepper.ui.scripts.LocalLanguageHandler
import it.diunito.pepper.ui.scripts.loadLanguages
import it.diunito.pepper.ui.theme.ClientTheme

class MainActivity : RobotActivity(), RobotLifecycleCallbacks {
    private lateinit var languageHandler: LanguageHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)

        val languages = loadLanguages(this)
        languageHandler = LanguageHandler(languages, initial = AppLanguage.IT)

        setContent {
            var forceDark by rememberSaveable { mutableStateOf<Boolean?>(null) }
            val effectiveDark = forceDark ?: isSystemInDarkTheme()

            ClientTheme(forceDark = forceDark) {
                CompositionLocalProvider(LocalLanguageHandler provides languageHandler) {
                    AppScaffold(
                        isDark = effectiveDark,
                        onToggleTheme = {
                            forceDark = !effectiveDark
                        },
                        onResetToSystem = {
                            forceDark = null
                        },
                        onAirlabClick = { null },
                        onDipInfoClick = { null },
                        onUnitoClick = { null },
                        languageHandler = languageHandler
                    ) { innerPadding ->
                        AppNavGraph(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }


    override fun onRobotFocusGained(qiContext: QiContext?) {
        // Log.i(TAG, "onRobotFocusGained")
    }

    override fun onRobotFocusLost() {
        // Log.i(TAG, "onRobotFocusLost")
    }

    override fun onRobotFocusRefused(reason: String?) {
        // Log.i(TAG, "onRobotFocusRefused")
    }

    override fun onDestroy() {
        QiSDK.unregister(this, this)
        super.onDestroy()
    }
}