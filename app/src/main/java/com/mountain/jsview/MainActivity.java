package com.mountain.jsview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        } else {
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        setContentView(R.layout.activity_main);
        ViewGroup viewById = (ViewGroup) findViewById(R.id.rl_content);
        JsInterface jsInterface = new JsInterface(viewById);
        JsEngine jsEngine = jsInterface.getJsEngine();
        TextView textView = (TextView) findViewById(R.id.button);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainRecycleViewActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        jsEngine.addJavascriptInterface(jsInterface, "JsInterface");
        jsEngine.invokeJsFunc("init",
                "{debug:true,screenWidth:" + displayMetrics.widthPixels + ",screenHeight:" + displayMetrics.heightPixels + "}");
        jsEngine.invokeJsFunc("createListViewAdapter");

    }
}
