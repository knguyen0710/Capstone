package com.juke.kynasaur.juke2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.spotify.sdk.android.player.Spotify;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.R.layout.simple_list_item_1;

public class AddToPlaylist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_playlist);

        final SpotifyApi api = new SpotifyApi();

        SpotifyService spotify = api.getService();

        api.setAccessToken(((MyApplication) this.getApplication()).getAccessToken());

        final HashMap<String, String> songAndId = new HashMap<>();

        spotify.getAlbumTracks("71WupOKqXgSrgg0CivZDHS", new Callback<Pager<Track>>() {
            @Override
            public void success(Pager<Track> tracks, Response response) {
                // specify that that trackList object is not of regular List object but also inherits from the Track object by putting it in carrots "List<Track>"
                List<Track> trackList = tracks.items;

                ListView listView = (ListView) findViewById(R.id.addSong);

                ArrayList<String> list = new ArrayList<>();
                if (trackList != null) {
                    for(Track t : trackList) {
                        list.add("+  " + t.name);
                        songAndId.put("+  " + t.name, t.uri);
                    }
                }

                ArrayAdapter adapter = new ArrayAdapter(AddToPlaylist.this,
                        android.R.layout.simple_list_item_1, list);

                listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        String item = ((TextView) view).getText().toString();
                        final MyApplication app = ((MyApplication) AddToPlaylist.this.getApplication());
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
