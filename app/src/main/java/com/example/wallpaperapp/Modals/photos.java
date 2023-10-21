package com.example.wallpaperapp.Modals;

import java.util.ArrayList;

public class photos {
    //curated Api response
    //search Modal
    public int page;
     public int per_page;

    private ArrayList<src> photos;

    public ArrayList<src> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<src> photos) {
        this.photos = photos;
    }

    public photos(ArrayList<src> photos) {
        this.photos = photos;
    }
}
