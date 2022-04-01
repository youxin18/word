package com.test.rem_word;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class InterfaceActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView right_nav;
    String word;
    String pron;
    String definition;
    public static List wordlist;
    int showNum;
    int dailyplan;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("UnionActivity","onCreate");
        Data.initWordList();
        setContentView(R.layout.activity_interface);

        getWordInfo(LoginUserPwdActivity.input_id);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        //滑动菜单
        right_nav=(NavigationView)findViewById(R.id.nav_right_view);
        right_nav.setItemIconTintList(null);

        right_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                if(item.getItemId()==R.id.nav_client){
                    Intent in3=new Intent(InterfaceActivity.this,Myprivacydata.class);
                    startActivity(in3);
                }
                else if(item.getItemId()==R.id.nav_base){
                    Intent in2=new Intent(InterfaceActivity.this,Mywordbase.class);
                    startActivity(in2);
                }
                else if(item.getItemId()==R.id.nav_instruction){
                    Intent in4=new Intent(InterfaceActivity.this,Myinstruction.class);
                    startActivity(in4);
                }
                return true;
            }
        });


        BottomNavigationView navView =(BottomNavigationView) findViewById(R.id.nav_view);

        navView.setItemIconTintList(null);


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_first, R.id.navigation_brush, R.id.navigation_fight,
                R.id.navigation_wrong).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        System.out.println("there is Interface");


    }
    private void getWordInfo(String username) {
        CreateDbHelper dbHelper = new CreateDbHelper(this, "user.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "Select*from recite where username=?";
        Log.d("Unionword",username);
        // Cursor cursor = db.query("recite",null,username+"=?",new String[]{LoginUserPwdActivity.input_id},null,null,null,"0,10");
        Cursor cursor=db.rawQuery(sql,new String[]{username});
        wordlist = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(this, "找到了！", Toast.LENGTH_SHORT).show();
                word = cursor.getString(cursor.getColumnIndex("word"));
                Log.d("Unionword",word);
                pron = cursor.getString(cursor.getColumnIndex("pron"));
                definition = cursor.getString(cursor.getColumnIndex("definition"));
                if (word == null || pron == null || definition == null) {
                    return;
                } else {
                    wordlist.add(word);

                }
                Log.d("Union", "word message:" + word + pron + definition + showNum);
            } while (cursor.moveToNext());
        }
        Log.d("Union", "wordlist:" + wordlist.size());
        cursor.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        Log.d("UnionActivity","onCreateOptionsMenu");
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                finish();
                break;
            case R.id.exit:
                Intent intent=new Intent("com.test.fragmentbestpractice.FORCE_OFFLINE");
                InterfaceActivity.this.sendBroadcast(intent);  //发送这个广播
                break;
            case R.id.setting:
                ToastUtil.showToast(this,"打开卡片式布局");
                Intent intent_card=new Intent(InterfaceActivity.this,cardview.class);
                startActivity(intent_card);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d("UnionActivity","onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("UnionActivity","onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d("UnionActivity","onRestart");
        super.onRestart();
    }
}