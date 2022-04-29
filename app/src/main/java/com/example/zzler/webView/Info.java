package com.example.zzler.webView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.zzler.R;
import com.example.zzler.main.MainActivity;

public class Info extends AppCompatActivity {

    private  WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        myWebView = (WebView) findViewById(R.id.infoView);
        myWebView.loadUrl("https://uoc.edu");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebViewClient webClient  = new WebViewClient();
        myWebView.setWebViewClient(webClient);

    }

    public void backInfo(View v){
        Intent i = new Intent (this, MainActivity.class);
        startActivity(i);
    }
}