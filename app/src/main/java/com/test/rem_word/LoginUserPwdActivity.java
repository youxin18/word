package com.test.rem_word;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginUserPwdActivity extends AppCompatActivity {
    private CreateDbHelper dbHelper;
    private EditText user;
    private EditText pwd;

    public static String input_id;
    public static String input_pwd;
    private String mid;
    private String mpwd;


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_pwd);
        TextView codeLogin = findViewById(R.id.code_login);
        TextView login = findViewById(R.id.login);

        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);
        /*dbHelper=new CreateDbHelper(this,"userInformation.db",null,1);
        SQLiteDatabase db =dbHelper.getWritableDatabase();*/

        codeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginUserPwdActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_id =user.getText().toString();
                input_pwd=pwd.getText().toString();
                login(input_id,input_pwd);
                if(input_id.equals(mid)&&input_pwd.equals(mpwd)){
                    Intent intent=new Intent(LoginUserPwdActivity.this,RMainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(LoginUserPwdActivity.this,"账户或密码不对，请核对后重试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @SuppressLint("Range")
    public void login(String username, String pwd) {
        dbHelper=new CreateDbHelper(this,"user.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select*from user1 where username=? and pwd=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, pwd});
        if (cursor.moveToFirst()) {
            do {
                mid = cursor.getString(cursor.getColumnIndex("username"));
                mpwd = cursor.getString(cursor.getColumnIndex("pwd"));
            } while (cursor.moveToNext());
        }
        cursor.close();


    }
}