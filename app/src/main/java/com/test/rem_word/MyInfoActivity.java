package com.test.rem_word;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MyInfoActivity extends AppCompatActivity {
    private EditText change_pwd;
    private EditText change_again_pwd;
    private TextView sure;
    String find_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        change_pwd = findViewById(R.id.change_pwd);
        change_again_pwd = findViewById(R.id.change_again_pwd);
        sure = findViewById(R.id.btn_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_text = change_pwd.getText().toString();
                String input_text_again = change_again_pwd.getText().toString();
                Log.d("Union","inputpwd"+input_text);
                if(input_text.length()==0||input_text_again.length()==0){
                    Toast.makeText(MyInfoActivity.this, "密码不能为空，请核对后重试！", Toast.LENGTH_SHORT).show();
                }else {
                    if (input_text.equals(input_text_again)) {
                        CreateDbHelper dbHelper = new CreateDbHelper(MyInfoActivity.this, "user.db", null, 1);
                        SQLiteDatabase put = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("pwd", input_text);
                        put.update("user1", values, "username=?", new String[]{LoginUserPwdActivity.input_id});
                        Toast.makeText(MyInfoActivity.this, "修改成功！请重新登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyInfoActivity.this, LoginUserPwdActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(MyInfoActivity.this, "两次输入的不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });
    }
}