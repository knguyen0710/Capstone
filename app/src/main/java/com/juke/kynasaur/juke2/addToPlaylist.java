package com.juke.kynasaur.juke2;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class addToPlaylist extends AppCompatActivity {

    String decade;
    String nineties = "71WupOKqXgSrgg0CivZDHS";
    String eighties = "4PbG9Ygfp3vDNhe9yKH1DN";
    String seventies = "2uRb5ak9Qyo1nhlTViw8MZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_playlist);
        decade = getIntent().getExtras().getString("decade");

        final SpotifyApi api = new SpotifyApi();

        SpotifyService spotify = api.getService();

        api.setAccessToken(((MyApplication) this.getApplication()).getAccessToken());

        final HashMap<String, String> songAndId = new HashMap<>();

        spotify.getAlbumTracks(decade, new Callback<Pager<Track>>() {
            @Override
            public void success(Pager<Track> tracks, Response response) {
                Log.d("DECADES", decade + "  " + nineties);
                TextView set_header = (TextView) findViewById(R.id.list_header);
                if (decade.equals(nineties)) {
                    set_header.setText("Hits from the 90's");
                } else if (decade.equals(eighties)) {
                    set_header.setText("Rockin' 80's");
                } else if (decade.equals(seventies)) {
                    set_header.setText("70's Boogie Beats");
                } else {
                    Log.d("FAILURE==", "decade variable not properly transferred from Searchable Activity");
                }

                // specify that that trackList object is not of regular List object but also inherits from the Track object by putting it in carrots "List<Track>"
                List<Track> trackList = tracks.items;

                ListView listView = (ListView) findViewById(R.id.addSong);

                ArrayList<String> list = new ArrayList<>();
                if (trackList != null) {
                    for (Track t : trackList) {
                        list.add("+  " + t.name);
                        songAndId.put("+  " + t.name, t.uri);
                    }
                }

                ArrayAdapter adapter = new ArrayAdapter(addToPlaylist.this,
                        android.R.layout.simple_list_item_1, list);


                listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        String item = ((TextView) view).getText().toString();
                        final MyApplication app = ((MyApplication) addToPlaylist.this.getApplication());
                        app.playList.add("songs", songAndId.get(item));
                        app.playList.saveInBackground();

                        Log.d("==SUCCESS==", songAndId.get(item));

                        Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }

        });


    }


}
