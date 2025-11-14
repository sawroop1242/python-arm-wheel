package com.jupyter.mobile.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.jupyter.mobile.data.ConnectionState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun JupyterWebView(
    url: String,
    modifier: Modifier = Modifier,
    onConnectionStateChanged: (ConnectionState) -> Unit = {},
    onWebViewCreated: (WebView) -> Unit = {}
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    javaScriptCanOpenWindowsAutomatically = true
                    mediaPlaybackRequiresUserGesture = false
                    allowFileAccess = true
                    allowContentAccess = true
                }
                
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onConnectionStateChanged(ConnectionState.Connecting)
                    }
                    
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        
                        // Inject mobile-friendly CSS
                        view?.evaluateJavascript("""
                            (function() {
                                var style = document.createElement('style');
                                style.innerHTML = `
                                    body {
                                        -webkit-text-size-adjust: 100%;
                                        touch-action: manipulation;
                                    }
                                    #header, .header {
                                        position: sticky !important;
                                        top: 0 !important;
                                        z-index: 1000 !important;
                                    }
                                    .container {
                                        max-width: 100% !important;
                                        padding: 8px !important;
                                    }
                                    .cell {
                                        margin: 8px 0 !important;
                                    }
                                    .CodeMirror {
                                        font-size: 14px !important;
                                        line-height: 1.4 !important;
                                    }
                                    .jp-Notebook {
                                        padding: 8px !important;
                                    }
                                    .jp-Cell {
                                        margin: 8px 0 !important;
                                    }
                                    .jp-CodeCell {
                                        font-size: 14px !important;
                                    }
                                    button, .btn {
                                        min-height: 44px !important;
                                        min-width: 44px !important;
                                        padding: 8px 12px !important;
                                    }
                                    input, select, textarea {
                                        font-size: 16px !important;
                                        padding: 8px !important;
                                    }
                                    .toolbar {
                                        flex-wrap: wrap !important;
                                    }
                                `;
                                document.head.appendChild(style);
                                
                                // Add viewport meta tag if not present
                                if (!document.querySelector('meta[name="viewport"]')) {
                                    var meta = document.createElement('meta');
                                    meta.name = 'viewport';
                                    meta.content = 'width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes';
                                    document.head.appendChild(meta);
                                }
                            })();
                        """.trimIndent(), null)
                        
                        onConnectionStateChanged(ConnectionState.Connected)
                    }
                    
                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        if (request?.isForMainFrame == true) {
                            onConnectionStateChanged(
                                ConnectionState.Error(
                                    error?.description?.toString() ?: "Connection failed"
                                )
                            )
                        }
                    }
                }
                
                webChromeClient = WebChromeClient()
                
                onWebViewCreated(this)
                
                if (url.isNotEmpty()) {
                    loadUrl(url)
                }
            }
        },
        update = { webView ->
            if (url.isNotEmpty() && webView.url != url) {
                webView.loadUrl(url)
            }
        }
    )
}
