package com.juke.kynasaur.juke2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.juke.kynasaur.juke2.models.Track;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Main3Activity extends AppCompatActivity {

    SpotifyApi api;
    SpotifyService spotify;

    private void startSpotifyStuff(){
        System.out.println("initializing Main3Activity");
        api = new SpotifyApi();
        api.setAccessToken(((MyApplication) this.getApplication()).getAccessToken());
        spotify = api.getService();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startSpotifyStuff();

        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MyApplication app = ((MyApplication) this.getApplication());

        ListView listView = (ListView) findViewById(R.id.listView);

        Track[] songs = {
                new Track("2 a.m.", "Adrian Marcel", "spotify:track:64Jyg9AzWl3AHdnkKPmY4T")
        };

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, songs);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();
//                Array code = item.split("code=");
//                app.addSomeSongs(code);
                Log.d("==THIS IS PRINTING:", item);
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });
    }

}
