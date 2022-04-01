package com.test.rem_word;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class brushFragment extends Fragment {
    private List<Word> wordList = new ArrayList<>();
    String word;
    String pron;
    String definition;
    int showNum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
Log.d("Union","createview");

        return inflater.inflate(R.layout.fragment_brush, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
       // initWordList();
        loadData(LoginUserPwdActivity.input_id);
        WordAdapter adapter = new WordAdapter(getActivity(),R.layout.word_item,wordList);
        ListView listView = (ListView) getActivity().findViewById(R.id.word_list_view);
        listView.setAdapter(adapter);
        Log.d("Union","onActivityCreated");


    }


    @SuppressLint("Range")
    private void loadData(String username) {
        CreateDbHelper createWordHelper=new CreateDbHelper(getContext(),"user.db",null,1);
        SQLiteDatabase aa=createWordHelper.getReadableDatabase();

        String sql = "Select*from brush where username=?";
        Cursor cursor = aa.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(getContext(),"单词显示成功！", Toast.LENGTH_SHORT).show();
                word= cursor.getString(cursor.getColumnIndex("word"));
                pron = cursor.getString(cursor.getColumnIndex("pron"));
                definition=cursor.getString(cursor.getColumnIndex("definition"));
                Word item = new Word(word,pron,definition,showNum,0);
                wordList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    private static int getNum(int endNum){
        if(endNum > 0){
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }

}