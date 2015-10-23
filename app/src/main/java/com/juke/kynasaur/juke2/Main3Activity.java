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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
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

        final MyApplication app = ((MyApplication) this.getApplication());

        ListView listView = (ListView) findViewById(R.id.listView);


        // CHANGE THIS TO BE FROM SONG LIST SO WHEN CLICKED IT ADDS TO PLAYLIST
        JSONArray testings = app.playList.getJSONArray("songs");

        ArrayList<String> list = new ArrayList<>();
        if (testings != null) {
            int len = testings.length();
            for (int i=0;i<len;i++){
                try {
                    list.add(testings.get(i).toString());
                } catch(JSONException e) {
                   Log.d("FAILURE", e.toString());
                }
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();

                Log.d("==THIS IS PRINTING:", item);
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });
    }

}
