package com.test.rem_word;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class CreateDbHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER="create table user1("+"username text primary key,"+"pwd text,"+"name text,"+"dailyplan Integer)";
    public static final String CREATE_RECITE="create table recite("+"username text ,"+"word text ,"+"pron text,"+"definition text)";
    public static final String CREATE_BRUSH="create table brush("+"username text,"+"word text ,"+"pron text,"+"definition text)";
    private Context mContext;
    public CreateDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
       db.execSQL(CREATE_RECITE);
       db.execSQL(CREATE_BRUSH);
        Toast.makeText(mContext,"Create succeeded", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user1");
        db.execSQL("drop table if exists recite");
        db.execSQL("drop table if exists brush");
        onCreate(db);

    }
}
