package com.pasir.logicsoft

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pasir.logicsoft.ui.theme.LogicSoftTheme
import androidx.compose.runtime.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        setContent {
            LogicSoftTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WebViewScreen("https://pasir2025.github.io/SIMULADOR-FUB/")
                }
            }
        }
    }
}

@Composable
fun WebViewScreen(url: String) {
    var webView: WebView? by remember { mutableStateOf(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.builtInZoomControls = false
                settings.displayZoomControls = false
                settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
                settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                clearCache(true)
                clearHistory()

                webViewClient = WebViewClient()
                loadUrl(url)
//                webView = this
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { view -> view.loadUrl(url) }
    )
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text("Vista previa del WebView (solo texto, no navegador)")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LogicSoftTheme {
        Greeting("Android")
    }
}