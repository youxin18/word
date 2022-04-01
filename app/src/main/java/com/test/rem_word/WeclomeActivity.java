package com.test.rem_word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class WeclomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weclome);
        handler.sendEmptyMessageDelayed(0,3000);
    }
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            getHome();
            super.handleMessage(msg);
        }
    };
 public void getHome(){
        Intent intent=new Intent(WeclomeActivity.this,EnterActivity.class);
        startActivity(intent);
        finish();

    }

}