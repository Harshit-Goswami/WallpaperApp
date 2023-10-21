package com.example.wallpaperapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.wallpaperapp.Modals.photos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    WallpaperAdapter adapter;
    RecyclerView recyclerView;
    SearchView search;
    FloatingActionButton next, prev;
    int page = 1;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);
        search = findViewById(R.id.search);
        next = findViewById(R.id.btn_next);
        prev = findViewById(R.id.btn_prev);

        CuratedPhotos();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                Search(query);
                return true;
            }
        });

        next.setOnClickListener(View -> {
            ++page;
            if (query == null || query.equals("")) CuratedPhotos();
            else Search(query);
        });
        prev.setOnClickListener(View -> {
            if (page == 1) {
                Toast.makeText(getApplicationContext(), "No prev Wallpaper", Toast.LENGTH_SHORT).show();
            } else {
                --page;
                if (query == null || query.equals("")) CuratedPhotos();
                else Search(query);
            }
        });

    }

    public void CuratedPhotos() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                RetrofitApi retrofitApi = new RetrofitApi(MainActivity.this);
                retrofitApi.getApiInterface().getWallpaper(page, 40).enqueue(new Callback<photos>() {
                    @Override
                    public void onResponse(Call<photos> call, Response<photos> response) {
                        if (response.isSuccessful()) {
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
                            assert response.body() != null;
                            adapter = new WallpaperAdapter(getApplicationContext(), response.body().getPhotos());
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<photos> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Failure..." + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        thread.start();
    }

    public void Search(String quary) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                new RetrofitApi(MainActivity.this).getApiInterface().getSearchWallpaper(quary, page, 80).enqueue(new Callback<photos>() {
                    @Override
                    public void onResponse(Call<photos> call, Response<photos> response) {
                        if (response.isSuccessful()) {
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
                            adapter = new WallpaperAdapter(getApplicationContext(), response.body().getPhotos());
                            recyclerView.setAdapter(adapter);

                        } else {
                            Toast.makeText(getApplicationContext(), "not able to get", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<photos> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), (CharSequence) t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        thread.start();
    }
}