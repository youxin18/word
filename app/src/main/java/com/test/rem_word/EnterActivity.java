package com.test.rem_word;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EnterActivity extends AppCompatActivity {
    private ImageView rightarrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_run);

    }


    @Override
    protected void onStart() {

        rightarrow = findViewById(R.id.rightarrow);
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterActivity.this, LoginActivity1.class);
                startActivity(intent);
                finish();

            }
        };
        rightarrow.setOnClickListener(clickListener);
        super.onStart();

    }


}
