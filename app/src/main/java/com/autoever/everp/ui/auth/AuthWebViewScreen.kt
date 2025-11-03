package com.autoever.everp.ui.auth

import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.autoever.everp.auth.AuthConfig

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AuthWebViewScreen(
    onCode: (code: String, codeVerifier: String) -> Unit,
    config: AuthConfig = AuthConfig.default(),
    vm: AuthViewModel = viewModel(),
) {
    LaunchedEffect(Unit) { vm.start(config) }

    val requestUri = vm.requestUri
    val authWebViewClient = remember(config) {
        object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url ?: return false
                return handleUrl(url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val parsed = url?.let { Uri.parse(it) } ?: return false
                return handleUrl(parsed)
            }

            private fun handleUrl(url: Uri): Boolean {
                val schemeOk = url.scheme == config.redirectScheme
                val hostOk = config.redirectHost?.let { url.host == it } ?: true
                val pathOk = url.path == config.redirectPath
                if (schemeOk && hostOk && pathOk) {
                    vm.handleRedirect(url, onCode)
                    return true
                }
                return false
            }
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (requestUri != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            settings.javaScriptEnabled = true
                            settings.cacheMode = WebSettings.LOAD_DEFAULT
                            this.webViewClient = authWebViewClient
                            loadUrl(requestUri.toString())
                        }
                    },
                    update = { webView ->
                        if (webView.url != requestUri.toString()) {
                            webView.loadUrl(requestUri.toString())
                        }
                    }
                )
            } else {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { vm.reset() }
    }
}
