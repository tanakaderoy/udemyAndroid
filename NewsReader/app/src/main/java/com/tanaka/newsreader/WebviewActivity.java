package com.tanaka.newsreader;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebviewActivity extends AppCompatActivity {
    WebView webView;
    String url = "";
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        setupWebView();
    }

    private void setupWebView() {
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);


        webView.getSettings().setJavaScriptEnabled(true);
        progressBar.setVisibility(View.VISIBLE);


        webView.setWebViewClient(new AppWebViewClients(progressBar));
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if (!url.equals("")) {
            webView.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
