package com.juke.kynasaur.juke2.sqlite;

import java.util.LinkedList;
import java.util.List;

import com.juke.kynasaur.juke2.models.Track;
        
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TrackDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create track table
        String CREATE_TRACK_TABLE = "CREATE TABLE tracks ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "artist TEXT, " +
                "code TEXT)";

        // create tracks table
        db.execSQL(CREATE_TRACK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tracks table if existed
        db.execSQL("DROP TABLE IF EXISTS tracks");

        // create fresh tracks table
        this.onCreate(db);
    }

    // CRUD METHODS --------------------------------------------------------------

    // Tracks table name
    private static final String TABLE_TRACKS = "tracks";

    // Tracks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_CODE = "code";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_ARTIST, KEY_CODE};

    public void addTrack(Track track){
        //for logging
        Log.d("addTrack", track.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, track.getTitle()); // get title
        values.put(KEY_ARTIST, track.getArtist()); // get artist
        values.put(KEY_CODE, track.getCode()); // get Spotify code

        // 3. insert
        db.insert(TABLE_TRACKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Track getTrack(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TRACKS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();


//         4. build track object
        Track track = new Track();
        track.setId(Integer.parseInt(cursor.getString(0)));
        track.setTitle(cursor.getString(1));
        track.setArtist(cursor.getString(2));
        track.setCode(cursor.getString(3));

        //log
        Log.d("getBook(" + id + ")", track.toString());

        // 5. return track
        return track;
    }

    public List<Track> getAllTracks() {
        List<Track> tracks = new LinkedList<Track>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TRACKS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build track and add it to list
        Track track = null;
        if (cursor.moveToFirst()) {
            do {
                track = new Track();
                track.setId(Integer.parseInt(cursor.getString(0)));
                track.setTitle(cursor.getString(1));
                track.setArtist(cursor.getString(2));
                track.setCode(cursor.getString(3));

                // Add track to tracks
                tracks.add(track);
            } while (cursor.moveToNext());
        }

        Log.d("getAllTracks()", tracks.toString());

        // return tracks
        return tracks;
    }
}
