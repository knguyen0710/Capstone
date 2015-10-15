package com.juke.kynasaur.juke2;

import android.app.Application;

/**
 * Created by kynasaur on 10/15/15.
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
