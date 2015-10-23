package com.juke.kynasaur.juke2;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OptionalDataException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.FeaturedPlaylists;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.QueryMap;
import retrofit.mime.TypedInput;

// THIS ACTIVITY USES THE SPOTIFY WRAPPER since the SDK only works for authentication (still in beta)
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

//  this method is called onClick because we define 'android:onClick="sMusicCall"' in <button> for content_main2.xml
    public void sMusicCall(View v) {
        SpotifyApi api = new SpotifyApi();

        SpotifyService spotify = api.getService();

//        define the access token with the global variable set in MyApplication.java through the AndroidManifest directly under <application>
        api.setAccessToken(((MyApplication) this.getApplication()).getAccessToken());

//        // OAUTH NOT WORKING FOR PLAYLIST CREATIION
//        final Map<String,Object> options=new HashMap<>();
//        options.put("name","My Playlist");
//        options.put("public", true);
//        spotify.createPlaylist("khn710", options, new Callback<Playlist>() {
//            @Override
//            public void success(Playlist playlist, Response response) {
//                Log.d("Successful", response.toString());
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d("Playlist failure", error.toString());
//            }
//        });


//  //Pager object is from Spotify and acts like pagination; must be imported
//         //Track object is similar
            spotify.getAlbumTracks("71WupOKqXgSrgg0CivZDHS", new Callback<Pager<Track>>() {
                @Override
                public void success(Pager<Track> tracks, Response response) {
                    // specify that that trackList object is not of regular List object but also inherits from the Track object by putting it in carrots "List<Track>"
                    List<Track> trackList = tracks.items;

                    String playIt = "";
                    for(Track t : trackList) {
                        playIt += t.name +" \n";
                    }
                    final TextView textViewToChange = (TextView) findViewById(R.id.textView);
                    textViewToChange.setText(playIt);


                    ParseObject ninetiesPlayList = new ParseObject("NinetiesPlayList");
                    ninetiesPlayList.put("90's music", Arrays.asList());
                    for(Track t : trackList ) {
                        ninetiesPlayList.addUnique("90's music", t.id);
                    ninetiesPlayList.saveInBackground();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("----Track list failure", error.toString());
                }
            });


//        spotify.getAlbum("71WupOKqXgSrgg0CivZDHS", new Callback<Album>() {
//            @Override
//            public void success(Album album, Response response) {
//                final TextView textViewToChange = (TextView) findViewById(R.id.textView);
//                textViewToChange.setText(album.name);
//                Log.d("Album success", album.name);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d("Album failure", error.toString());
//            }
//        });
    }

}
