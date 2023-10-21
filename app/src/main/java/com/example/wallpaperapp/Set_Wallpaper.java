package com.example.wallpaperapp;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;

public class Set_Wallpaper extends AppCompatActivity {
    ImageView img_wallpaper;
    MaterialTextView btn_setWallpaper;
    String wallpaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_walpaper);
//        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        img_wallpaper = findViewById(R.id.setWallpaper);
        btn_setWallpaper = findViewById(R.id.btn_SetWallpaper);
        ImageButton download = (ImageButton) findViewById(R.id.btn_download);

        Intent intent = getIntent();
        wallpaper = intent.getStringExtra("wallpaper");
//        Picasso.get().load(wallpaper).into(img_wallpaper);
        Glide.with(this).load(wallpaper).into(img_wallpaper);
//        setWallpaper(wallpaper);
        btn_setWallpaper.setOnClickListener(v -> {
            Bitmap bitmap =((BitmapDrawable) img_wallpaper.getDrawable()).getBitmap();
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(Set_Wallpaper.this);
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            setWallpaper(wallpaper);
            Toast.makeText(getApplicationContext(), "Set Successfully", Toast.LENGTH_SHORT).show();
        });

        download.setOnClickListener(View -> {
            if (requestRuntimePermission()) {
                try {
                    downloadWallpaper();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // For retrieving favourite data using shared preferences
            }
        });
    }

    // For RunTime Permission
    private Boolean requestRuntimePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    13
            );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 13) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                downloadWallpaper();

            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        13
                );
            }
        }

    }

    void downloadWallpaper() {
        DownloadManager downloadManager = null;
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(wallpaper);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(wallpaper)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, wallpaper + ".jpg");
        downloadManager.enqueue(request);
        Toast.makeText(getApplicationContext(), "Downloading.....", Toast.LENGTH_SHORT).show();
    }
  /*  void setWallpaper(String url){
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.e("Bitmap","onBitmapLoaded");
                WallpaperManager wm = WallpaperManager.getInstance(getApplicationContext());
                try {
                    wm.setBitmap(bitmap);
                } catch (IOException e) {
                    Log.e("Bitmap","Err -"+e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("Bitmap","onBitmapFailed -"+e.getMessage());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e("Bitmap","onPreparedLoad");
            }
        };
        Picasso .get().load(url).into(target);
    }*/
}