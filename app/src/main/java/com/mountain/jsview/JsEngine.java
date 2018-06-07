package com.mountain.jsview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class JsEngine {
    WebView mWebView;
    private boolean hasIntit;
    private static final String TAG = "JsEngine";
    private static final String jsContextString = "file:///android_asset/index.html";
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsEngine(Context context) {
        mWebView = new WebView(context);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//打开js和安卓通信

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e(TAG, "onPageFinished: " + url);
                if (jsContextString.equals(url)) {
                    hasIntit = true;
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.loadUrl(jsContextString);//本地模板
        hasIntit = false;
    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(Object object, String name) {
        mWebView.addJavascriptInterface(object, name);
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void invokeJsFunc(final String funcName, final String... args) {
        if (hasIntit) {
            invokeJsFuncInternal(funcName, args);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    invokeJsFunc(funcName, args);
                }
            });
        }
    }

    private void invokeJsFuncInternal(String funcName, String... args) {
        StringBuffer funcBuilder = new StringBuffer("javascript:");
        funcBuilder.append(funcName)
                .append("(");
        if (args != null) {
            int len = args.length;
            for (int i = 0; i < len; i++) {
                funcBuilder.append(args[i]);
                if (i != len - 1) {
                    funcBuilder.append(",");
                }
            }
        }
        funcBuilder.append(")");
        String function = funcBuilder.toString();
        Log.d(TAG, function);
        mWebView.loadUrl(function);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            mWebView.loadUrl("javascript:callJS()");
//        } else {
//            mWebView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    //此处为 js 返回的结果
//                }
//            });
//        }
    }


}
