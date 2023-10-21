package com.example.wallpaperapp;

import com.example.wallpaperapp.Modals.photos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface{

    public String BASE_URL ="https://api.pexels.com/v1/";

    @Headers({"Accept: application/json",
            "Authorization: 563492ad6f917000010000011a3074bc3e2c4594a4dabb60696cee77"})
    @GET("curated/")
    Call<photos> getWallpaper(
            @Query("page") int page,//@Query("page") int page,
            @Query("per_page") int per_page);

    @Headers("Authorization: 563492ad6f917000010000011a3074bc3e2c4594a4dabb60696cee77")
    @GET("search")
    Call<photos> getSearchWallpaper(
            @Query("query") String query,
            @Query("page") int page,
            @Query("per_page") int per_page );
}
