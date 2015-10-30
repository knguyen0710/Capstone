package com.juke.kynasaur.juke2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

public class Queue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final MyApplication app = ((MyApplication) this.getApplication());

        JSONArray songQueue = app.playList.getJSONArray("songs");
        String songList = "";
        if (songQueue.length() > 0) {
            for (int i = 0; i < songQueue.length(); i++) {
                try {
                    songList += songQueue.get(i) + "\n";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
