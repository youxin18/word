package com.test.rem_word;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class failActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        TimerTask task = new TimerTask(){
            public void run(){
                Intent intent = new Intent(failActivity.this,InterfaceActivity.class);
                startActivity(intent);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,1500);
    }
}