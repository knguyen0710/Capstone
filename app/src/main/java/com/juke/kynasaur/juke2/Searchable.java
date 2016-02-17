package com.juke.kynasaur.juke2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Searchable extends Activity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private ListView myList;
    private SearchView searchView;
    private MyCustomAdapter defaultAdapter;

    final ArrayList<String> resultList = new ArrayList<>();

    final SpotifyApi api = new SpotifyApi();

    SpotifyService spotify = api.getService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        //relate the listView from java to the one created in xml
        myList = (ListView) findViewById(R.id.list);

        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this nameList and for producing
        // a view to represent an item in that data set.
        defaultAdapter = new MyCustomAdapter(Searchable.this, resultList);
        myList.setAdapter(defaultAdapter);

        //prepare the SearchView
        searchView = (SearchView) findViewById(R.id.search);

        //Sets the default or resting state of the search field. If true, a single search icon is shown by default and
        // expands to show the text field and other buttons when pressed.
        //The default value is true.
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onClose() {
        myList.setAdapter(defaultAdapter);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.isEmpty()) {
            displayResults(query);
        } else {
            myList.setAdapter(defaultAdapter);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() < 1) {
            Button button = (Button) findViewById(R.id.nineties);
            button.setVisibility(View.VISIBLE);
            Button button1 = (Button) findViewById(R.id.eighties);
            button1.setVisibility(View.VISIBLE);
            Button button2 = (Button) findViewById(R.id.seventies);
            button2.setVisibility(View.VISIBLE);

            TextView text = (TextView) findViewById(R.id.suggestions);
            text.setText(R.string.no_songs_yet);

            ListView listView = (ListView) findViewById(R.id.list);
            listView.setVisibility(View.GONE);
        }
        myList.setAdapter(defaultAdapter);
        return false;
    }

//    RESULTS DISPLAYED AFTER THE QUERY TEXT HAS BEEN SUBMITTED via THIS METHOD
    private void displayResults(final String query) {
        HashMap<String, Object> options = new HashMap();
        options.put("limit", 5);
        if (query != null) {
            spotify.searchTracks(query, options, new Callback<TracksPager>() {
                @Override
                public void success(TracksPager tracksPager, Response response) {
                    Button button = (Button) findViewById(R.id.nineties);
                    button.setVisibility(View.INVISIBLE);
                    Button button2 = (Button) findViewById(R.id.eighties);
                    button2.setVisibility(View.INVISIBLE);
                    Button button3 = (Button) findViewById(R.id.seventies);
                    button3.setVisibility(View.INVISIBLE);

                    TextView text = (TextView) findViewById(R.id.suggestions);
                    text.setText("Top 5 search results for '" + query + "'");

                    ListView listView = (ListView) findViewById(R.id.list);
                    listView.setVisibility(View.VISIBLE);

                    List<Track> tracks = tracksPager.tracks.items;
                    Log.d("SEARCHED==", tracks.get(0).toString());

                    final HashMap<String, String> songAndId = new HashMap<>();
                    ArrayList<String> resultList = new ArrayList<>();
                    for(Track t : tracks) {

                        resultList.add("+  " + t.name + " by " + t.artists.get(0).name);
                        songAndId.put("+  " + t.name + " by " + t.artists.get(0).name, t.uri);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(Searchable.this,
                            android.R.layout.simple_list_item_1, resultList);

                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            String item = ((TextView) view).getText().toString();
                            final MyApplication app = ((MyApplication) Searchable.this.getApplication());
                            app.playList.add("songs", songAndId.get(item));
                            app.playList.saveInBackground();

                            Log.d("==SUCCESS==", songAndId.get(item));

                            Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                        }
                    });


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("FAILURE", error.toString());
                }
            });
        }
    }

    public void nineties(View v) {
        Intent intent = new Intent(Searchable.this, addToPlaylist.class);
        intent.putExtra("decade", "71WupOKqXgSrgg0CivZDHS");
        startActivity(intent);
    }

    public void eighties(View v) {
        Intent intent = new Intent(Searchable.this, addToPlaylist.class);
        intent.putExtra("decade", "4PbG9Ygfp3vDNhe9yKH1DN");
        startActivity(intent);
    }

    public void seventies(View v) {
        Intent intent = new Intent(Searchable.this, addToPlaylist.class);
        intent.putExtra("decade", "2uRb5ak9Qyo1nhlTViw8MZ");
        startActivity(intent);
    }

}
