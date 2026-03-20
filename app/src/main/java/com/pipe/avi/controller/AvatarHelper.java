package com.pipe.avi.controller;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.webkit.WebViewAssetLoader;

import java.util.Locale;

public class AvatarHelper {

    private Context context;
    private WebView webView;
    private TextToSpeech tts;
    private boolean isTtsReady = false;

    public AvatarHelper(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
        setupAvatar(context, webView);
        initTTS(context);
    }

    private void initTTS(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("es", "ES"));
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTtsReady = true;
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // La animación se inicia justo antes de llamar a tts.speak en el hilo principal
            }

            @Override
            public void onDone(String utteranceId) {
                setSpeaking(false);
            }

            @Override
            public void onError(String utteranceId) {
                setSpeaking(false);
            }
        });
    }

    public static void setupAvatar(Context context, WebView webView) {
        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(context))
                .build();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(false);
        webSettings.setAllowContentAccess(false);
        
        webView.setOnTouchListener((v, event) -> true);
        webView.setClickable(false);
        webView.setFocusable(false);
        webView.setBackgroundColor(0);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
        });

        webView.loadUrl("https://appassets.androidplatform.net/assets/avatar_viewer.html");
    }

    public void speak(String text) {
        if (!isTtsReady || text == null || text.isEmpty()) return;

        // Estimar duración: aprox 100ms por carácter es una métrica simple, 
        // pero mejor dejar que el UtteranceProgressListener maneje el final.
        // Iniciamos animación Talk
        setSpeaking(true);
        
        // Reproducir audio
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "avatar_speech");
    }

    public void setSpeaking(boolean speaking) {
        String anim = speaking ? "Talk" : "Idle";
        webView.post(() -> webView.evaluateJavascript("playAnimation('" + anim + "')", null));
    }

    public void animarHablar(int durationMs) {
        webView.post(() -> webView.evaluateJavascript("playAnimation('Talk', " + durationMs + ")", null));
    }
    
    public void ejecutarAnimacion(String anim, int durationMs) {
        webView.post(() -> webView.evaluateJavascript("playAnimation('" + anim + "', " + durationMs + ")", null));
    }

    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
