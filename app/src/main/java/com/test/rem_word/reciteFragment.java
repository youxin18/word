package com.test.rem_word;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.test.rem_word.InterfaceActivity.wordlist;


public class reciteFragment extends Fragment {


    private TextView knowButton, unknowButton;
    private ImageButton tipsButton;
    private TextView wordText, definitionText;
    public static List wordnumber=new ArrayList();
    public static List wrongword=new ArrayList();
    private TextView my_plan;
    private TextView today_num;
    private TextView my_info;
    private TextView logout;
    String word;
    String pron;
    String definition;
    int showNum;
    int dailyplan;
    int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recite, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        knowButton = getActivity().findViewById(R.id.knowButton);
        unknowButton = getActivity().findViewById(R.id.unknowButton);
        tipsButton = (ImageButton) getActivity().findViewById(R.id.tipsButton);
        wordText = (TextView) getActivity().findViewById(R.id.wordText);
        today_num = getActivity().findViewById(R.id.today_num);
        logout=getActivity().findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),LoginUserPwdActivity.class);
                startActivity(intent);
            }
        });
        my_plan = getActivity().findViewById(R.id.my_plan);
        my_info=getActivity().findViewById(R.id.my_info);
        my_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MyInfoActivity.class);
                startActivity(intent);
            }
        });

        getData(LoginUserPwdActivity.input_id);

        my_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();


            }
        });

        definitionText = (TextView) getActivity().findViewById(R.id.definitionText);
        for (int i = 0; i< Integer.parseInt(today_num.getText().toString()); i++){
            wordnumber.add(wordlist.get(i));
            Log.d("Union","wordnumberlist:"+wordnumber.size());
        }
        wordText.setText(wordnumber.get(0).toString());
        definitionText.setText("");
        String lastword=wordlist.get((Integer.parseInt(today_num.getText().toString()))-1).toString();
        Log.d("lastword","lastword"+lastword);
        super.onActivityCreated(savedInstanceState);
        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                definitionText.setText("");

                Collections.shuffle(wordnumber);
                String a = (String) wordnumber.get(0);
                wordText.setText(a);

                if (wordText.getText().toString().equals(lastword)){
                    Intent intent=new Intent(getContext(),FinishActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        unknowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                definitionText.setText("");
                wrongword.add(wordText.getText().toString());
                Collections.shuffle(wordnumber);
                String a = (String) wordnumber.get(0);
                wordText.setText(a);

            }
        });
        tipsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                definitionText.setText(getDefinition(wordText.getText().toString()));
            }
        });
    }

    private void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.shareDialog);
        final AlertDialog dialog = builder
                .setView(R.layout.activity_plan)
                .setCancelable(false)
                .create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        NumberPicker numberPicker = dialog.getWindow().findViewById(R.id.choose_number);
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(50);
        int a = Integer.parseInt((today_num.getText()).toString());
        numberPicker.setValue(a);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int value = numberPicker.getValue();
                CreateDbHelper dbHelper = new CreateDbHelper(getContext(), "user.db", null, 1);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put("dailyplan", value);
                db.update("user1", values, "username=?", new String[]{LoginUserPwdActivity.input_id});
                Log.d("Union", "update dailyplan success");
                today_num.setText(String.valueOf(value));

            }
        });
        dialog.getWindow().findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "yes", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }

    private void getData(String username) {
        CreateDbHelper dbHelper = new CreateDbHelper(getContext(), "user.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "Select*from user1 where username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(getContext(), "找到了！", Toast.LENGTH_SHORT).show();
                dailyplan = cursor.getInt(cursor.getColumnIndex("dailyplan"));
                Log.d("Union", "dailyplan:" + dailyplan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (dailyplan==0){
            CreateDbHelper dbHelper1 = new CreateDbHelper(getContext(), "user.db", null, 1);
            SQLiteDatabase db1 = dbHelper.getReadableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("dailyplan","10");
            db1.update("user1", contentValues, "username=?", new String[]{LoginUserPwdActivity.input_id});
            Log.d("Union", "update dailyplan 10 success");
            getData(LoginUserPwdActivity.input_id);
        }else {
            today_num.setText(String.valueOf(dailyplan));
        }
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