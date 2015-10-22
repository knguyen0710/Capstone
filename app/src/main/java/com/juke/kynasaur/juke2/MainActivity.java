package com.juke.kynasaur.juke2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

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

        Parse.initialize(this, "NnUyDFvhp0q1CqMpYyMNYfKlJzJI7eNcbbhnu9cL", "CCW2gwpmLwHExTcHAbEzk7ZaeNYRFSvue74oaAzO");

        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

// other fields can be set just like with ParseObject
        user.put("phone", "650-555-0000");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.d("SUCCESS--", "Woop! It worked");
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.d("FAILURE --", e.toString());
                }
            }
        });
//  // PARSE RESPONSE OBJECT FOR TEST USER
//  //   { "session_token": "uy7IGNYkMhvrwo2tzeS5KoZTb", "id": "XKcqaTKGCx", "data": { "email": "email@example.com", "username": "my name", "phone": "650-555-0000" }, "created_at": "2015-10-22T18:40:09Z", "updated_at": "2015-10-22T18:40:09Z" }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
               // SAVING SONGS TO THE PARSE DB
                ParseObject playList = new ParseObject("PlayList");
                playList.put("songs", Arrays.asList("spotify:track:0xCmwofyCiXdhoBsMSNj2w", "spotify:track:3NFuE3uDOr6QUw9UZ9HzKo"));
                playList.saveInBackground();

//                set the access token to a variable to be used across activities
                        ((MyApplication) this.getApplication()).setAccessToken(response.getAccessToken());
//                set MyApplication variable to pull playlist variable on line 72
               final MyApplication app = ((MyApplication) this.getApplication());
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    // use "void" for methods in which nothing is explicitly returned
                    // here, a song is played onInitialized but nothing is actually returned
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        // PLAYLIST CREATION - AUTOMATED PLAY AFTER EACH SONG
                        app.addSomeSongs("spotify:track:0xCmwofyCiXdhoBsMSNj2w");
                        mPlayer.play(app.getSomeSongs());
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
        // TO DO -- add functionality to pause current song
        mPlayer.pause();
    }

    public void playIt(View v) {
        // TO DO -- add functionality to play current queued song
        mPlayer.resume();
    }

    public void songList(View v) {
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
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