package com.juke.kynasaur.juke2;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Arrays;
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

    ParseObject playList = new ParseObject("PlayList");

    @Override
    public void onCreate() {
        super.onCreate();

        // //   INITIALIZATION HERE CONNECTS THE APP TO THE PARSE DB
        Parse.initialize(this, "NnUyDFvhp0q1CqMpYyMNYfKlJzJI7eNcbbhnu9cL", "CCW2gwpmLwHExTcHAbEzk7ZaeNYRFSvue74oaAzO");


        // SAVING SONGS TO THE PARSE DB
        playList.put("songs", Arrays.asList());


    }

}
