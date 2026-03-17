package com.pipe.avi.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pipe.avi.R;

public class AvatarHelper {

    private final Context context;
    private final WebView webView;
    private MediaPlayer mediaPlayer;

    public AvatarHelper(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
        configurarWebView();
    }

    private void configurarWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setBackgroundColor(0); // Transparente
        
        // Evitar que abra el navegador externo
        webView.setWebViewClient(new WebViewClient());
        
        webView.loadUrl("file:///android_asset/avatar_viewer.html");
    }

    /**
     * Carga el avatar y opcionalmente reproduce un sonido
     * @param rawSoundResourceId ID del recurso de audio (ej: R.raw.bienvenida) o 0 si no hay sonido
     */
    public void cargarAvatarConSonido(int rawSoundResourceId) {
        if (rawSoundResourceId != 0) {
            detenerSonido();
            mediaPlayer = MediaPlayer.create(context, rawSoundResourceId);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> detenerSonido());
                mediaPlayer.start();
            }
        }
    }

    public void detenerSonido() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Permite cambiar el modelo 3D dinámicamente llamando a la función JS definida en el HTML
     */
    public void cambiarModelo(String urlGlb) {
        webView.evaluateJavascript("loadModel('" + urlGlb + "')", null);
    }
}