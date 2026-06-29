package it.diunito.pepper

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import it.diunito.pepper.ui.components.overlay.AppScaffold
import it.diunito.pepper.ui.navigation.AppNavGraph
import it.diunito.pepper.ui.scripts.AppLanguage
import it.diunito.pepper.ui.scripts.LanguageHandler
import it.diunito.pepper.ui.scripts.LocalLanguageHandler
import it.diunito.pepper.ui.scripts.loadLanguages
import it.diunito.pepper.ui.theme.ClientTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var languageHandler: LanguageHandler

    // QiSDK references held as Any? to avoid hard class-load on non-Pepper devices
    private var qiSdkRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Attempt QiSDK registration — fails gracefully on emulator / non-Pepper devices
        tryRegisterQiSDK()

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

    override fun onDestroy() {
        tryUnregisterQiSDK()
        super.onDestroy()
    }

    // ═══════════════════════════════════════════════════════════
    // QiSDK helpers — wrapped in try/catch so the app runs
    // on both Pepper (with robot features) and standard devices
    // (emulator / tablet) without crashing.
    // ═══════════════════════════════════════════════════════════

    private fun tryRegisterQiSDK() {
        try {
            val qiSdkClass = Class.forName("com.aldebaran.qi.sdk.QiSDK")
            val callbackClass = Class.forName("com.aldebaran.qi.sdk.RobotLifecycleCallbacks")

            // Create a dynamic proxy for RobotLifecycleCallbacks
            val proxy = java.lang.reflect.Proxy.newProxyInstance(
                callbackClass.classLoader,
                arrayOf(callbackClass)
            ) { _, method, args ->
                when (method.name) {
                    "onRobotFocusGained" -> {
                        Log.i(TAG, "onRobotFocusGained")
                    }
                    "onRobotFocusLost" -> {
                        Log.i(TAG, "onRobotFocusLost")
                    }
                    "onRobotFocusRefused" -> {
                        Log.i(TAG, "onRobotFocusRefused: ${args?.getOrNull(0)}")
                    }
                }
                null
            }

            // QiSDK.register(activity, callbacks)
            val registerMethod = qiSdkClass.getMethod(
                "register",
                android.app.Activity::class.java,
                callbackClass
            )
            registerMethod.invoke(null, this, proxy)
            qiSdkRegistered = true
            Log.i(TAG, "QiSDK registered successfully — running on Pepper")
        } catch (e: Exception) {
            qiSdkRegistered = false
            Log.w(TAG, "QiSDK not available — running in emulator/standard device mode", e)
        }
    }

    private fun tryUnregisterQiSDK() {
        if (!qiSdkRegistered) return
        try {
            val qiSdkClass = Class.forName("com.aldebaran.qi.sdk.QiSDK")
            val callbackClass = Class.forName("com.aldebaran.qi.sdk.RobotLifecycleCallbacks")
            val unregisterMethod = qiSdkClass.getMethod(
                "unregister",
                android.app.Activity::class.java,
                callbackClass
            )
            // We can't easily unregister a proxy, but calling with null callbacks is safe
            // In practice, onDestroy on Pepper disposes the activity anyway
            Log.i(TAG, "QiSDK unregister skipped (proxy lifecycle)")
        } catch (e: Exception) {
            Log.w(TAG, "QiSDK unregister failed", e)
        }
    }
}