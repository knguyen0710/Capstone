package com.juke.kynasaur.juke2;

import android.app.Application;

/**
 THIS FILE ALLOWS GLOBAL ACCES TO THE ACCESS TOKEN
 */
public class MyApplication extends Application {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
