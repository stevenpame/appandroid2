package com.pipe.avi.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AvatarHelper {

    private Context context;
    private WebView webView;
    private MediaPlayer mediaPlayer;

    public AvatarHelper(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
        setupAvatar(webView);
    }

    public static void setupAvatar(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        
        // Habilitar acceso a archivos locales (assets)
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        
        // Desactivar interacciones para que el usuario no mueva el avatar
        webView.setOnTouchListener((v, event) -> true);
        webView.setClickable(false);
        webView.setFocusable(false);

        webView.setBackgroundColor(0); // Transparente
        
        // Importante: Aceleración de hardware para evitar errores Mali
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebViewClient(new WebViewClient());
        
        // Carga el HTML desde assets
        webView.loadUrl("file:///android_asset/avatar_viewer.html");
    }

    public void cargarAvatarConSonido(int resId) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        
        setSpeaking(true);
        
        mediaPlayer = MediaPlayer.create(context, resId);
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> {
                setSpeaking(false);
                mp.release();
                mediaPlayer = null;
            });
            mediaPlayer.start();
        } else {
            setSpeaking(false);
        }
    }

    private void setSpeaking(boolean speaking) {
        String anim = speaking ? "Talk" : "Idle";
        webView.post(() -> webView.evaluateJavascript("playAnimation('" + anim + "')", null));
    }

    public void animarHablar(int durationMs) {
        webView.post(() -> webView.evaluateJavascript("playAnimation('Talk', " + durationMs + ")", null));
    }

    public void saludar() {
        animarHablar(2000);
    }

    public void destroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
