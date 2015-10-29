package com.juke.kynasaur.juke2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

// TO-DO: MOVE SPOTIFY LOG IN FROM MAIN ACTIVITY TO HOME ACTIVITY

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "3e17129f06ca474c857c509c5ee0a2e0";

    private static final String REDIRECT_URI = "juke2://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

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

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);

//                set the access token to a variable to be used across activities
                        ((MyApplication) this.getApplication()).setAccessToken(response.getAccessToken());
//                set MyApplication variable to pull playlist variable on line 72
               final MyApplication app = ((MyApplication) this.getApplication());
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    // use "void" for methods in which nothing is explicitly returned
                    // here, a song is played onInitialized but nothing is actually returned


                    @Override
                    public void onInitialized(Player player) {
                        player.addConnectionStateCallback(MainActivity.this);
                        player.addPlayerNotificationCallback(MainActivity.this);

                        // PLAYLIST FROM PARSE - AUTOMATED PLAY AFTER EACH SONG
                        JSONArray songQueue = app.playList.getJSONArray("songs");
                        if (songQueue.length() > 1) {
                            List songList = new ArrayList();
                            for (int i = 0; i < songQueue.length(); i++) {
                                try {
                                    songList.add(songQueue.get(i));
                                } catch (JSONException e) {
                                    Log.d("ERROR==", e.toString());
                                }
                            }
                            player.play(songList);

                        } else {
                            TextView noSongs = (TextView)findViewById(R.id.needSongs);
                            noSongs.setVisibility(View.VISIBLE);

                            Button bPlay, bPause, bSkip;
                            bPlay = (Button)findViewById(R.id.bPlay);
                            bPause = (Button)findViewById(R.id.pause_button);
                            bSkip = (Button)findViewById(R.id.bSkip);

                            bPlay.setVisibility(View.INVISIBLE);
                            bPause.setVisibility(View.INVISIBLE);
                            bSkip.setVisibility(View.INVISIBLE);


                        }


//                        USE PLAYER NOTIFICATION TO SEE END OF SONG TO DISPLAY CURRENT SONG??
                        player.addPlayerNotificationCallback(new PlayerNotificationCallback() {

                            @Override
                            public void onPlaybackEvent (EventType eventType, PlayerState playerState){
                                if (eventType == EventType.TRACK_CHANGED || eventType == EventType.BECAME_INACTIVE) {
                                    mPlayer.getPlayerState(new PlayerStateCallback() {
                                        @Override
                                        public void onPlayerState(PlayerState playerState) {
                                            Log.d("SUCCESS==", playerState.trackUri);
                                        }
                                    });
                                }
                            }

                            public void onPlaybackError(ErrorType errorType, String string) {
                                Log.d("FAILURE==", string);
                            };
                        });
                    }



                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("===MainActivity", "Could not initialize player: " + throwable.getMessage());
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
        Intent intent = new Intent(MainActivity.this, addToPlaylist.class);
        intent.putExtra("decade", "71WupOKqXgSrgg0CivZDHS");
        startActivity(intent);

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
        switch (eventType) {
            // Handle event type as necessary
            default:
                break;
        }
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
}