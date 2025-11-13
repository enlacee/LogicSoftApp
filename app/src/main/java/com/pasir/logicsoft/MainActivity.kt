package com.pasir.logicsoft

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.pasir.logicsoft.BuildConfig
import com.pasir.logicsoft.ui.theme.LogicSoftTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WebViewScreen(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                setLayerType(View.LAYER_TYPE_HARDWARE, null)

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    builtInZoomControls = true
                    displayZoomControls = false
                    cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    setSupportZoom(true)
                    allowFileAccess = true
                }

                // 🔥 Clave para permitir gestos táctiles reales
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
                setOnTouchListener { v, event ->
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> v.parent?.requestDisallowInterceptTouchEvent(true)
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.parent?.requestDisallowInterceptTouchEvent(false)
                    }
                    v.performClick() // mantener accesibilidad
                    false // no interceptar el evento
                }

                webChromeClient = WebChromeClient()
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        view.evaluateJavascript(TOUCH_TO_MOUSE_JS, null)
                    }
                }

                // Limpieza antes de cargar
                clearCache(true)
                clearHistory()

                loadUrl(url)
            }
        },

        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { false } // permitir que WebView gestione los eventos
    )
}

private const val TOUCH_TO_MOUSE_JS = """
    (function () {
        if (window.__TOUCH_TO_MOUSE_POLYFILLED__) return;
        window.__TOUCH_TO_MOUSE_POLYFILLED__ = true;

        function triggerMouse(type, touch) {
            const simulated = new MouseEvent(type, {
                bubbles: true,
                cancelable: true,
                view: window,
                clientX: touch.clientX,
                clientY: touch.clientY,
                screenX: touch.screenX,
                screenY: touch.screenY,
                buttons: type === 'mouseup' ? 0 : 1
            });
            touch.target.dispatchEvent(simulated);
        }

        function onTouchStart(event) {
            const touch = event.changedTouches[0];
            triggerMouse('mouseenter', touch);
            triggerMouse('mouseover', touch);
            triggerMouse('mousemove', touch);
            triggerMouse('mousedown', touch);
        }

        function onTouchMove(event) {
            const touch = event.changedTouches[0];
            triggerMouse('mousemove', touch);
        }

        function onTouchEnd(event) {
            const touch = event.changedTouches[0];
            triggerMouse('mouseup', touch);
            triggerMouse('mouseout', touch);
            triggerMouse('mouseleave', touch);
            triggerMouse('click', touch);
        }

        document.addEventListener('touchstart', onTouchStart, true);
        document.addEventListener('touchmove', onTouchMove, true);
        document.addEventListener('touchend', onTouchEnd, true);
        document.addEventListener('touchcancel', onTouchEnd, true);
    })();
"""

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