package com.juke.kynasaur.juke2;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;


/**
 THIS FILE ALLOWS GLOBAL ACCESS TO THE SPOTIFY TOKEN
 */
public class MyApplication extends Application {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private List someSongs = new ArrayList();

    public List getSomeSongs() {
        return someSongs;
    }

    public void addSomeSongs(String song) {
        this.someSongs.add(song);
    }

}
