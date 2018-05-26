package com.mountain.jsview;

import android.util.Log;
import android.webkit.WebView;

public class JsEngine {
    WebView mWebView;
    private static final String TAG = "JsEngine";
    public JsEngine(WebView webView) {
        this.mWebView = webView;
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void invokeJsFunc(String funcName, String... args) {
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
