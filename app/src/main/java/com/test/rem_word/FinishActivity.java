package com.test.rem_word;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class FinishActivity extends AppCompatActivity {
private LinearLayout try_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
delete();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        try_again=findViewById(R.id.try_again);
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FinishActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void delete() {
        CreateDbHelper dbHelper = new CreateDbHelper(FinishActivity.this, "user.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        for (int i=0;i<reciteFragment.wordnumber.size();i++){
           /* String sql = "Select*from recite where word=?";
            Cursor cursor = db.rawQuery(sql, new String[]{reciteFragment.wordnumber.get(i).toString()});
            if (cursor.moveToFirst()) {
                do {
                    Toast.makeText(this, "找到了！", Toast.LENGTH_SHORT).show();
                    String word = cursor.getString(cursor.getColumnIndex("definition"));
                    Log.d("Union", "delete word:" + word);*/
                    db.delete("recite","word=?",new String[]{reciteFragment.wordnumber.get(i).toString()});
                    Log.d("Union", "delete word:" + reciteFragment.wordnumber.get(i).toString()+"success");
/*
                } while (cursor.moveToNext());
            }
            db.close();*/
        }



    }
}