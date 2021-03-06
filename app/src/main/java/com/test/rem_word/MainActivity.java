package com.test.rem_word;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import jxl.Sheet;
import jxl.Workbook;

/*
 *??????BaseActivity??????????????????????????????????????????ArrayList???????????????????????????????????????
 * ???????????????????????????
 */
public class MainActivity extends BaseActivity implements android.view.View.OnClickListener{
    private ViewPager mviewpager;
    private PagerAdapter mpagerAdapter;
    private List<View> mviews=new ArrayList<View>();

    //2 tab,??????tab??????????????????
    private LinearLayout mtabmainpage;
    private LinearLayout mtabmine;
    private LinearLayout mtabsearch;

    //2?????????
    private ImageButton mmainpage;
    private ImageButton mmine;
    private ImageButton msearch;

    //????????????
    private int flag=0;
    private boolean flag1=true;

    //??????????????????
    private boolean flagtab3=true;

    //??????????????????
    PopupWindow mpopupwindow=null;

    //?????????????????????view
    public View pop2view;

    //????????????
    public static final int TAKE_PHOTO=1;
    public ImageView picture;
    private Uri imageUri;
    //????????????
    public static final int CHOOSE_PHOTO=2;

    //????????????
    private DrawerLayout mDrawerLayout;

    //??????
    private SharedPreferences pref=null;
    private SharedPreferences.Editor editor;

    //???????????????
    private NavigationView navView=null;

    //?????????
    public boolean flagrewrite=false;
    public String tempstr=null;

    //????????????
    public boolean isExit;

    //?????????
    public boolean flagdesti=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT>=21){
//            View decorView=getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.parseColor("#8000"));
//        }
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        //????????????
        navView=(NavigationView)findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                if(item.getItemId()==R.id.nav_client){
                    Intent in3=new Intent(MainActivity.this,Myprivacydata.class);
                    startActivity(in3);
                }
                else if(item.getItemId()==R.id.nav_base){
                    Intent in2=new Intent(MainActivity.this,Mywordbase.class);
                    startActivity(in2);
                }
                else if(item.getItemId()==R.id.nav_instruction){
                    Intent in4=new Intent(MainActivity.this,Myinstruction.class);
                    startActivity(in4);
                }
                return true;
            }
        });

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        //????????????
//        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Snackbar.make(v,"??????????????????",Snackbar.LENGTH_SHORT)
//                        .setAction("??????",new View.OnClickListener(){//????????????
//                            @Override
//                            public void onClick(View view){
//                                ToastUtil.showToast(MainActivity.this,"????????????");
//                            }
//                        }).show();
//            }
//        });

        //ImageButton???????????????
        initView();
        initviewPage();
        initEvent();
    }

    private void initEvent(){
        mtabmainpage.setOnClickListener(this);
        mtabmine.setOnClickListener(this);
        mtabsearch.setOnClickListener(this);
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageSelected(int arg0){
                int currentItem=mviewpager.getCurrentItem();
                switch(currentItem){
                    case 0:
                        resetImg(flag);
                        flag=0;
                        TextView word_count=layoutmainpage.findViewById(R.id.word_count);
                        if(pref.getInt("count",-1)!=-1){
                            word_count.setText(pref.getInt("count",0)+"  ???");
                        }
                        mmainpage.setImageResource(R.drawable.select);
                        break;
                    case 2:
                        resetImg(flag);
                        flag=2;
                        if(flag1){
                            //???????????????????????????
                            setmonitor();
                            flag1=false;
                        }
                        mmine.setImageResource(R.drawable.select);
                        break;
                    case 1:
                        resetImg(flag);
                        flag=1;
                        msearch.setImageResource(R.drawable.select);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrolled(int arg0,float arg1,int arg2){}
            @Override
            public void onPageScrollStateChanged(int arg0){}
        });
    }

    private void initView(){
        mviewpager=(ViewPager)findViewById(R.id.viewpager);
        //???????????????LinearLayour
        mtabmainpage=(LinearLayout)findViewById(R.id.id_1);
        mtabmine=(LinearLayout)findViewById(R.id.id_2);
        mtabsearch=(LinearLayout)findViewById(R.id.id_3);
        //?????????2?????????
        mmainpage=(ImageButton)findViewById(R.id.id_1img);
        mmainpage.setImageResource(R.drawable.select);//???????????????????????????
        mmine=(ImageButton)findViewById(R.id.id_2img);
        msearch=(ImageButton)findViewById(R.id.id_3img);
    }
    private View layoutmainpage=null;
    private View layoutsearch=null;
    private View layoutmine=null;
    private void initviewPage(){
        //?????????????????????
        LayoutInflater mlayoutinflater= LayoutInflater.from(this);
        layoutmainpage=mlayoutinflater.inflate(R.layout.tab3,null);
        layoutsearch=mlayoutinflater.inflate(R.layout.tab1,null);
        layoutmine=mlayoutinflater.inflate(R.layout.tab2,null);
        mviews.add(layoutmainpage);
        mviews.add(layoutsearch);
        mviews.add(layoutmine);

        //?????????
        TextView finddes=findViewById(R.id.finddesti);
        finddes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View c){
                if(flagdesti==true){
                    flagdesti=false;
                    finddes.setText("");
                    finddes.setVisibility(View.GONE);
                }
            }
        });

        //??????????????????
        TextView addmyword=layoutmainpage.findViewById(R.id.addword);
        addmyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,add_word.class);
                startActivity(in);
            }
        });

        //?????????????????????
        TextView checkmydata=layoutmainpage.findViewById(R.id.englishbase);
        checkmydata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in2=new Intent(MainActivity.this,Mywordbase.class);
                startActivity(in2);
            }
        });

        //???????????????????????????
        mpagerAdapter=new PagerAdapter(){
            @Override
            public void destroyItem(ViewGroup container,int position,Object object){
                container.removeView(mviews.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container,int position){
                View view=mviews.get(position);
                container.addView(view);
                return view;
            }
            @Override
            public boolean isViewFromObject(View arg0,Object arg1){
                return arg0==arg1;
            }
            @Override
            public int getCount(){
                return mviews.size();
            }
        };
        mviewpager.setAdapter(mpagerAdapter);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        String username=pref.getString("name",null);
        TextView username1=layoutmainpage.findViewById(R.id.name);
        EditText myname=layoutmine.findViewById(R.id.myname);
        ImageView rewrite=layoutmine.findViewById(R.id.rewritename);
        View headerview=navView.getHeaderView(0);//??????????????????????????????
        TextView username2=headerview.findViewById(R.id.username);
        if(username!=null){
            myname.setText(username);
            username1.setText(username);
            username2.setText(username);
        }
        else{
            myname.setText("?????????");
            username1.setText("?????????");
            username2.setText("?????????");
        }
        layoutmine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                HideSoftInput(v2.getWindowToken());
            }
        });
        myname.setCursorVisible(false);
        myname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewrite.setImageDrawable(getResources().getDrawable(R.drawable.yes));
                myname.setCursorVisible(true);
                flagrewrite=true;
            }
        });
        rewrite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(flagrewrite==true){
                    myname.setCursorVisible(false);
                    rewrite.setImageDrawable(getResources().getDrawable(R.drawable.rewrite));
                    flagrewrite=false;
                    tempstr=myname.getText().toString();
                    if(tempstr.equals("")){
                        ToastUtil.showToast(MainActivity.this,"??????????????????");
                    }
                    else{
                        editor=pref.edit();
                        editor.putString("name",tempstr);
                        editor.apply();
                        myname.setText(tempstr);
                        username1.setText(tempstr);
                        username2.setText(tempstr);
                        ToastUtil.showToast(MainActivity.this,"???????????????");
                    }
                }
                else{
                    myname.setCursorVisible(true);
                    myname.setSelection(myname.length());
                }
            }
        });
        TextView word_count=layoutmainpage.findViewById(R.id.word_count);
        if(pref.getInt("count",-1)!=-1){
            word_count.setText(pref.getInt("count",0)+"  ???");
        }
        setheadphoto();

        //?????????
        Button find=layoutmainpage.findViewById(R.id.search_word);
        EditText searchtext=layoutmainpage.findViewById(R.id.editsearch);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWord(searchtext.getText().toString());
                searchtext.setText("");
            }
        });

        //???????????????????????????
        ImageView changephoto=layoutmainpage.findViewById(R.id.icon_image);
        changephoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ToastUtil.showToast(MainActivity.this,"?????????????????????????????????");
            }
        });

        //????????????????????????
        if(flagtab3){
            flagtab3=false;
            //??????
            LinearLayout v1=layoutmainpage.findViewById(R.id.panelmain);
            TextView startmemory=layoutmainpage.findViewById(R.id.startmemory);
            v1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    HideSoftInput(v.getWindowToken());
                }
            });
            startmemory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mviewpager.setCurrentItem(1);
                    resetImg(flag);
                    flag=1;
                    msearch.setImageResource(R.drawable.select);
                }
            });
        }

        //????????????


    }

    private void findWord(String word){
        if(word.equals("")){
            ToastUtil.showToast(MainActivity.this,"???????????????????????????");
            return;
        }
        int start=0;
        int end=0;
        int array[]={0,96,150,301,393,473,523,558,594,679,686,690,720,777,799,829,937,949,1025,1163,1227,1238,1265,1282,1282,1286,1287};
        int temp=word.charAt(0);
        if(temp<97){
            temp=temp+32;
        }
        else if(word.substring(0,1).equals(" ")){
            ToastUtil.showToast(MainActivity.this,"??????????????????????????????????????????");
        }
        else if(temp>=97){
            start=array[temp-97];
            end=array[temp-96];
            searchinfile(word,start,end);
        }
        else{
            ToastUtil.showToast(MainActivity.this,"????????????????????????????????????");
        }
    }

    private void searchinfile(String defiword,int start,int end){
        AssetManager manager=MainActivity.this.getAssets();
        TextView findesti=findViewById(R.id.finddesti);
        try{
            Workbook workbook=Workbook.getWorkbook(manager.open("glossary.xls"));
            Sheet sheet=workbook.getSheet(0);//????????????
            //??????
            for(int j=start;j<end;j++){
                if(defiword.equals(sheet.getCell(0,j).getContents())){
                    findesti.setVisibility(View.VISIBLE);
                    flagdesti=true;
                    findesti.setText(defiword+"\n  "+sheet.getCell(1,j).getContents());
                    //ToastUtil.showToast(MainActivity.this,defiword+"  "+sheet.getCell(1,j).getContents());
                    break;
                }
                else if(j==end-1){
                    if(flagdesti==true){
                        findesti.setVisibility(View.GONE);
                        flagdesti=false;
                    }
                    ToastUtil.showToast(MainActivity.this,"???????????????????????????");
                }
            }
            workbook.close();
        }
        catch(Exception e){
            if(flagdesti==true){
                findesti.setVisibility(View.GONE);
                flagdesti=false;
            }
            ToastUtil.showToast(MainActivity.this,"??????????????????????????????");
        }
    }

    private void setheadphoto(){
        //?????????????????????
        CircleImageView head=layoutmine.findViewById(R.id.myheadphoto);
        CircleImageView headmain=layoutmainpage.findViewById(R.id.icon_image);
        View headerview=navView.getHeaderView(0);//??????????????????????????????
        CircleImageView headnav=headerview.findViewById(R.id.icon_image2);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        String urlpic=pref.getString("picture",null);
        File pic=null;
        if(urlpic==null){
            pic=new File("");
        }
        else{
            pic=new File(urlpic);
        }
        if(!pic.exists()){
            ToastUtil.showToast(MainActivity.this,"????????????????????????????????????????????????");
        }
        if(urlpic!=null&&pic.exists()){
            Glide.with(MainActivity.this).load(urlpic).into(head);
            Glide.with(MainActivity.this).load(urlpic).into(headmain);
            Glide.with(MainActivity.this).load(urlpic).into(headnav);
        }
        else{
            ToastUtil.showToast(MainActivity.this,"??????????????????");
        }
    }

    private void resetImg(int flag1){
        if(flag1==0){
            mmainpage.setImageResource(R.drawable.mainpage);
        }
        else if(flag1==1){
            msearch.setImageResource(R.drawable.search);
        }
        else if(flag1==2){
            mmine.setImageResource(R.drawable.mine);
        }
    }

    private void setmonitor(){
        TextView mydata=(TextView) findViewById(R.id.mydata);
        mydata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in3=new Intent(MainActivity.this,Myprivacydata.class);
                startActivity(in3);
//                ToastUtil.showToast(MainActivity.this,"??????????????????");
            }
        });

        TextView instruction=(TextView) findViewById(R.id.instructions);
        instruction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in2=new Intent(MainActivity.this,Mywordbase.class);
                startActivity(in2);
//                ToastUtil.showToast(MainActivity.this,"????????????");
            }
        });
        TextView setting=(TextView) findViewById(R.id.settings);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in4=new Intent(MainActivity.this,Myinstruction.class);
                startActivity(in4);
//                ToastUtil.showToast(MainActivity.this,"????????????");
            }
        });
        ImageView myheadphoto=(ImageView)findViewById(R.id.myheadphoto);
        myheadphoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pop2view=showpopupwindow();
                View tab2panel=(View)findViewById(R.id.tab2panel);
                tab2panel.setBackgroundColor(Color.rgb(192,192,192));
            }
        });
    }
    //?????????????????????????????????????????????
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    //????????????
    public View showpopupwindow() {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popuplayout, null);
        mpopupwindow = new PopupWindow(contentView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT, true);
        mpopupwindow.setContentView(contentView);
        TextView tv1=(TextView)contentView.findViewById(R.id.pop_camera);
        TextView tv2=(TextView)contentView.findViewById(R.id.pop_album);
        TextView cancel=(TextView)contentView.findViewById(R.id.cancel);
        View poppanel=(View)findViewById(R.id.tab2panel);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        cancel.setOnClickListener(this);
        poppanel.setOnClickListener(this);
        View rootview=LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
        mpopupwindow.showAtLocation(rootview, Gravity.BOTTOM,0,0);
        //??????popupwindow????????????????????????
        mpopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                View tab2panel=(View)findViewById(R.id.tab2panel);
                tab2panel.setBackgroundColor(Color.WHITE);
            }
        });
        return contentView;
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.id_1:
                mviewpager.setCurrentItem(0);
                resetImg(flag);
                TextView word_count=layoutmainpage.findViewById(R.id.word_count);
                if(pref.getInt("count",-1)!=-1){
                    word_count.setText(pref.getInt("count",0)+"  ???");
                }
                flag=0;
                mmainpage.setImageResource(R.drawable.select);
                break;
            case R.id.id_2:
                mviewpager.setCurrentItem(2);
                resetImg(flag);
                if(flag1){
                    //???????????????????????????
                    setmonitor();
                    flag1=false;
                }
                flag=2;
                mmine.setImageResource(R.drawable.select);
                break;
            case R.id.id_3:
                mviewpager.setCurrentItem(1);
                resetImg(flag);
                flag=1;
                msearch.setImageResource(R.drawable.select);
                break;
            case R.id.cancel:
                mpopupwindow.dismiss();
                break;
            case R.id.pop_camera:
                //??????????????????
                changeheadpohto();
                mpopupwindow.dismiss();
                break;
            case R.id.pop_album:
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else{
                    openAlbum();
                }
                mpopupwindow.dismiss();
                break;
        }
    }
    //??????????????????????????????
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    //??????????????????
    public void changeheadpohto(){
        File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }
        catch(IOException e){
            ToastUtil.showToast(MainActivity.this,"??????????????????");
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(MainActivity.this,"com.test.yingfunews.fileprovider",outputImage);
        }
        else{
            imageUri= Uri.fromFile(outputImage);
        }
        //????????????
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            //????????????
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        //?????????????????????
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture=(ImageView)findViewById(R.id.myheadphoto);
                        bitmap=Bitmap.createScaledBitmap(bitmap,90,90,true);//???????????????????????????
                        picture.setImageBitmap(bitmap);
                        File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                        pref = PreferenceManager.getDefaultSharedPreferences(this);
                        editor=pref.edit();//??????editor??????
                        editor.putString("picture",outputImage.getAbsolutePath());
                        editor.apply();
                        setheadphoto();
                    }
                    catch(FileNotFoundException e){
                        ToastUtil.showToast(MainActivity.this,"????????????????????????");
                    }
                }
                break;
            case CHOOSE_PHOTO:
                //????????????
                if(resultCode==RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);//4.4???????????????????????????
                    } else {
                        //4.4??????
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    //????????????
    private void openAlbum(){
        Intent intent=null;
        if(Build.VERSION.SDK_INT>=19){//?????????????????????????????????
            intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //intent.setAction(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
            //intent.setType("image/*");
        }
        else{
            intent=new Intent();
            intent.setAction("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
        }
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
                else{
                    ToastUtil.showToast(MainActivity.this,"?????????????????????");
                }
                break;
            case 3:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    ToastUtil.showToast(this,"?????????????????????????????????????????????");
                }
            default:
                break;
        }
    }
    //????????????4.4????????????
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //?????????document??????uri?????????id??????
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//????????????????????????id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if("com.android.provider.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }
        else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }
        else if("file".equalsIgnoreCase(uri.getScheme())){
            //file??????uri
            imagePath=uri.getPath();
        }
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();//??????editor??????
        editor.putString("picture",imagePath);
        editor.apply();
        setheadphoto();
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();//??????editor??????
        editor.putString("picture",imagePath);
        editor.apply();
        setheadphoto();
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        //??????uri???selection??????????????????????????????
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //??????
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            bitmap=Bitmap.createScaledBitmap(bitmap,90,90,true);
            picture=(ImageView)findViewById(R.id.myheadphoto);
            picture.setImageBitmap(bitmap);
        }
        else{
            ToastUtil.showToast(MainActivity.this,"??????????????????");
        }
    }

    //???????????????
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
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
                MainActivity.this.sendBroadcast(intent);  //??????????????????
                break;
            case R.id.setting:
                ToastUtil.showToast(this,"?????????????????????");
                Intent intent_card=new Intent(MainActivity.this,cardview.class);
                startActivity(intent_card);
                break;
            default:
                break;
        }
        return true;
    }

    /*
    ???????????????
     */
    private void HideSoftInput(IBinder token){
        if(token!=null){
            InputMethodManager manager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        else{
            return super.onKeyDown(keyCode,event);
        }
    }
    public void exit(){
        Handler mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                isExit=false;
            }
        };
        if(!isExit){
            TextView finddestin=findViewById(R.id.finddesti);
            if(flagdesti==true){
                finddestin.setVisibility(View.GONE);
                flagdesti=false;
                return;
            }
            isExit=true;
            ToastUtil.showToast(MainActivity.this,"??????????????????????????????");
            mHandler.sendEmptyMessageDelayed(0,2000);
        }
        else{
            System.exit(0);
        }
    }

}