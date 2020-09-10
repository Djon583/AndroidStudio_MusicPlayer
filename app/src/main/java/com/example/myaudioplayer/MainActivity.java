package com.example.myaudioplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    static ArrayList<MusicField> musicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();

    }

    //mkdir Folder
    private void createAndCheckFolder() {
        String myfolder = Environment.getExternalStorageDirectory() + "/ASD";
        File f = new File(myfolder);
        if (!f.exists())
            if (!f.mkdir()) {
                Log.e("Path : " + myfolder, "Album : can't be created folder ASD");
                //Toast.makeText(this, myfolder+" can't be created.", Toast.LENGTH_SHORT).show();
            } else{
                Log.e("Path : " + myfolder, "Album : can be created folder ASD");
                //Toast.makeText(this, myfolder+" can be created.", Toast.LENGTH_SHORT).show();
            }
        else {
            Log.e("Path : " + myfolder, "Album : folder ASD already exits.");
            //Toast.makeText(this, myfolder+" already exits.", Toast.LENGTH_SHORT).show();
        }
    }

    //=============================Разрешение на использование внутренный хранилище
    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }else{
            createAndCheckFolder();
            musicFiles = getAllAudio(this);
            initViewPager();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        if(requestCode == REQUEST_CODE){
            if(grantResult[0] == PackageManager.PERMISSION_GRANTED){
                createAndCheckFolder();
                musicFiles = getAllAudio(this);
                initViewPager();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);

            }
        }
    }
    //=====================================================================

    //========================Показать таб меню
    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SongsFragment(), "Songs");
        viewPagerAdapter.addFragments(new AlbumFragment(), "Album");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    //=========================Настройка таю меню
    public static class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    //Список Музыки
    public static ArrayList<MusicField> getAllAudio(Context context){
        ArrayList<MusicField> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null,null,null);
        if(cursor != null ){
            while(cursor.moveToNext()){
                //if(cursor.getString(0).equals("ASD")){
                    String album = cursor.getString(0);
                    String title = cursor.getString(1);
                    String duration = cursor.getString(2);
                    String path = cursor.getString(3);
                    String artist = cursor.getString(4);
                    MusicField musicField = new MusicField(path,title, artist, album, duration);
                    Log.e("Path : " + path, "Album : " + album);
                    tempAudioList.add(musicField);
                //}
            }
            cursor.close();
        }

        return tempAudioList;

    }
}