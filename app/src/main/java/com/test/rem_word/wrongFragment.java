package com.test.rem_word;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class wrongFragment extends Fragment {

    private List<Word> wordList = new ArrayList<>();
    String word;
    String pron;
    String definition;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrong, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        for (int i=0;i<reciteFragment.wrongword.size();i++){
            word= (String) reciteFragment.wrongword.get(i);
            definition=getDefinition(word);
            pron=getPron(word);
            Word item = new Word(word,pron,definition,0,0);
            wordList.add(item);

        }


        WordAdapter adapter = new WordAdapter(getActivity(),R.layout.word_item,wordList);
        ListView listView = (ListView) getActivity().findViewById(R.id.wrong_list_view);
        listView.setAdapter(adapter);
    }
    private String getDefinition(String wordtext) {
        CreateDbHelper dbHelper = new CreateDbHelper(getContext(), "user.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "Select*from recite where word=?";
        Cursor cursor = db.rawQuery(sql, new String[]{wordtext});
        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(getContext(), "找到了！", Toast.LENGTH_SHORT).show();
                definition = cursor.getString(cursor.getColumnIndex("definition"));
                Log.d("Union", "definition:" + definition);
            } while (cursor.moveToNext());
        }

        return definition;
    }
    private String getPron(String wordtext) {
        CreateDbHelper dbHelper = new CreateDbHelper(getContext(), "user.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "Select*from recite where word=?";
        Cursor cursor = db.rawQuery(sql, new String[]{wordtext});
        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(getContext(), "找到了！", Toast.LENGTH_SHORT).show();
                pron = cursor.getString(cursor.getColumnIndex("pron"));
                Log.d("Union", "pron:" + pron);
            } while (cursor.moveToNext());
        }

        return pron;
    }
}