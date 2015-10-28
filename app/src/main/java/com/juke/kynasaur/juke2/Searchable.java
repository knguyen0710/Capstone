package com.juke.kynasaur.juke2;

        import android.app.Activity;
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
//    RESULT LIST REPLACES EXAMPLE NAME LIST
    private ArrayList<String> resultList;

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
//        defaultAdapter = new MyCustomAdapter(Searchable.this, resultList);
//        myList.setAdapter(defaultAdapter);

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
        myList.setAdapter(defaultAdapter);
        return false;
    }

    /**
     * Method used for performing the search and displaying the results. This method is called every time a letter
     * is introduced in the search field.
     *
     * @param query Query used for performing the search
     */
    private void displayResults(final String query) {
        HashMap<String, Object> options = new HashMap();
        options.put("limit", 5);
        if (query != null) {
            spotify.searchTracks(query, options, new Callback<TracksPager>() {
                @Override
                public void success(TracksPager tracksPager, Response response) {
                    List<Track> tracks = tracksPager.tracks.items;
                    Log.d("SEARCHED==", tracks.get(0).toString());

                    final HashMap<String, String> songAndId = new HashMap<>();
                    ListView listView = (ListView) findViewById(R.id.addSong);

                    for(Track t : tracks) {
                        resultList.add("+  " + t.name);
                        songAndId.put("+  " + t.name, t.uri);
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
//        Cursor cursor = mDbHelper.searchByInputText((query != null ? query : "@@@@"));
//
//        if (cursor != null) {
//
//            String[] from = new String[]
//
//            // Specify the view where we want the results to go
//            int[] to = new int[] {R.id.search_result_text_view};
//
//            // Create a simple cursor adapter to keep the search data
//            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.result_search_item, cursor, from, to);
//            myList.setAdapter(cursorAdapter);
//
//            // Click listener for the searched item that was selected
//            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    // Get the cursor, positioned to the corresponding row in the result set
//                    Cursor cursor = (Cursor) myList.getItemAtPosition(position);
//
//                    // Get the state's capital from this row in the database.
//                    String selectedName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//                    Toast.makeText(MyActivity.this, selectedName, Toast.LENGTH_SHORT).show();
//
//                    // Set the default adapter
//                    myList.setAdapter(defaultAdapter);
//
//                    // Find the position for the original list by the selected name from search
//                    for (int pos = 0; pos < nameList.size(); pos++) {
//                        if (nameList.get(pos).equals(selectedName)){
//                            position = pos;
//                            break;
//                        }
//                    }
//
//                    // Create a handler. This is necessary because the adapter has just been set on the list again and
//                    // the list might not be finished setting the adapter by the time we perform setSelection.
//                    Handler handler = new Handler();
//                    final int finalPosition = position;
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            myList.setSelection(finalPosition);
//                        }
//                    });
//
//                    searchView.setQuery("",true);
//                }
//            });

//        }
    }
}
