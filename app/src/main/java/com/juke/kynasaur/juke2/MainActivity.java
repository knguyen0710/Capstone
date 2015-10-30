package com.juke.kynasaur.juke2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "3e17129f06ca474c857c509c5ee0a2e0";

    private static final String REDIRECT_URI = "juke2://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int AUTHENTICATION_REQUEST_CODE = 1337;

    private Player mPlayer;

    private SpotifyService spotify;

    // use @override for nonspecific methods that can be applied to any activity/call/instance/etc.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // this sets up the initial sign in for the user
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, AUTHENTICATION_REQUEST_CODE, request);

        final SpotifyApi api = new SpotifyApi();

        spotify = api.getService();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        final List songList = new ArrayList();

        // Check if result comes from the correct activity
        if (requestCode == AUTHENTICATION_REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {

                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);

//                set the access token to a variable to be used across activities
                        ((MyApplication) this.getApplication()).setAccessToken(response.getAccessToken());
//                set MyApplication variable to pull playlist variable on line 72
               final MyApplication app = ((MyApplication) this.getApplication());
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {

                    @Override
                    public void onInitialized(final Player player) {
                        player.addConnectionStateCallback(MainActivity.this);
                        player.addPlayerNotificationCallback(MainActivity.this);

                        // PLAYLIST FROM PARSE - AUTOMATED PLAY AFTER EACH SONG
                        JSONArray songQueue = app.playList.getJSONArray("songs");
                        if (songQueue.length() > 0) {

                            for (int i = 0; i < songQueue.length(); i++) {
                                try {
                                    songList.add(songQueue.get(i));
                                } catch (JSONException e) {
                                    Log.d("ERROR==", e.toString());
                                }
                            }
                            player.play(songList);
                            Log.d("PARSE SONGS==", songList.get(0).toString());

                        } else {
                            player.play("spotify:track:3NFuE3uDOr6QUw9UZ9HzKo");

                            TextView noSongs = (TextView)findViewById(R.id.needSongs);
                            noSongs.setVisibility(View.VISIBLE);

                            Button bPlay, bPause, bSkip;
                            bPlay = (Button)findViewById(R.id.bPlay);
                            bPause = (Button)findViewById(R.id.pause_button);
                            bSkip = (Button)findViewById(R.id.bSkip);

//                            bPlay.setVisibility(View.INVISIBLE);
//                            bPause.setVisibility(View.INVISIBLE);
//                            bSkip.setVisibility(View.INVISIBLE);
                            Log.d("NOT PARSE SONGS", "WHERE IS IT");

                        }


//                        USE PLAYER NOTIFICATION TO SEE END OF SONG TO DISPLAY CURRENT SONG??
                        player.addPlayerNotificationCallback(new PlayerNotificationCallback() {

                            @Override
                            public void onPlaybackEvent (EventType eventType, PlayerState playerState){
                                Log.d("THIS IS THE EVENT==", eventType.toString());
//                                MUST USE TRACK CHANGED AS EVENT - "PLAYING" EVENT RESULTS IN CONTINUOUS CALLS DURING PLAY AND ERRORS OUT
                                if (eventType.equals(EventType.TRACK_CHANGED)){
                                    String uri = playerState.trackUri;
                                    String id = uri.substring(14);
                                    GetSongTitle title = new GetSongTitle(id);
                                    title.execute();

                                    TextView findTitle = (TextView) findViewById(R.id.playing);
                                    TextView noSongs = (TextView) findViewById(R.id.needSongs);

                                    findTitle.setVisibility(View.VISIBLE);
                                    noSongs.setVisibility(View.INVISIBLE);
                                    Log.d("NOW PLAYING==", playerState.trackUri);
                                } else if (eventType.name().equals("TRACK_END") || eventType.equals(EventType.SKIP_NEXT)) {
                                    String uri = playerState.trackUri;

                                    JSONArray songQueue = app.playList.getJSONArray("songs");
                                    if (songQueue.length() > 0) {
                                        for (int i = 0; i < songQueue.length(); i++) {
                                            try {
                                                songList.add(songQueue.get(i));
                                            } catch (JSONException e) {
                                                Log.d("ERROR==", e.toString());
                                            }
                                        }
                                        Integer remove = songList.indexOf(uri);
                                        for (int k = 0; k <= remove; k++) {
                                            songList.remove(k);
                                        }
                                        player.play(songList);

                                    }
                                } else if (eventType.equals(EventType.END_OF_CONTEXT)){
                                    Log.d("THE END==", "SHOULD HIT THIS AT THE END OF THE PLAYLIST");
                                } else {
                                    Log.d("EVENTS UNACCOUNTED==", "PAUSE, AUDIO_FLUSH");
                                }
                            }



                            @Override
                            public void onPlaybackError(ErrorType errorType, String s) {

                            }
                    });

                }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                });
            }
        }
    }

    public void pauseIt(View v) {
        mPlayer.pause();
    }

    public void playIt(View v) {
        mPlayer.resume();
    }

    public void nextIt(View v) {
        mPlayer.skipToNext();
    }

    public void songList(View v) {
        startActivity(new Intent(MainActivity.this, Searchable.class));
    }

    public void queueIt(View v) {
        startActivity(new Intent(MainActivity.this, Queue.class));
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");

    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());


    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

//  RUNS ASYNCHRONOUS TASK TO MAKE API CALL - CALLS ON THE MAIN THREAD RESULT IN ERROR
    private class GetSongTitle extends AsyncTask<URL, Void, String> {
        private String base = "https://api.spotify.com/v1/tracks/";

        public GetSongTitle(String url) {
            this.base = base + url;
        }

        @Override
        protected String doInBackground(URL... urls) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                        .url(base)
                        .build();

            try {
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();

                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject album = jsonObject.getJSONObject("album");

                String title = album.get("name").toString();
                return title;
            } catch (IOException e) {
                Log.d("HTTP ERROR==", e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String title) {
            super.onPostExecute(title);
            TextView resetTitle = (TextView) findViewById(R.id.playing);

            resetTitle.setText(title);
        }
    }
}