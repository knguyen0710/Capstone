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

        // //     user creation is not necessary but can be used to sort permissions (admin vs moderator)
//        ParseUser user = new ParseUser();
//        user.setUsername("my name");
//        user.setPassword("my pass");
//        user.setEmail("email@example.com");

// other fields can be set just like with ParseObject
//        user.put("phone", "650-253-0000");
//
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(com.parse.ParseException e) {
//                if (e == null) {
//                    // Hooray! Let them use the app now.
//                } else {
//                    Log.d("FAILURE", String.valueOf(e.getLocalizedMessage()));
//                    // Sign up didn't succeed. Look at the ParseException
//                    // to figure out what went wrong
//                }
//            }
//        });

        // SAVING SONGS TO THE PARSE DB
        playList.put("songs", Arrays.asList());
        playList.add("songs", "spotify:track:3NFuE3uDOr6QUw9UZ9HzKo");
        playList.add("songs", "spotify:track:22mek4IiqubGD9ctzxc69s");

    }

}
