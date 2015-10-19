package com.juke.kynasaur.juke2.models;


/**
 * Created by kynasaur on 10/17/15.
 */
public class Track {
    private int id;
    private String title;
    private String artist;
    private String code;

    public Track() {}

    public Track(String title, String artist, String code) {
        super();
        this.title = title;
        this.artist = artist;
        this.code = code;
    }

    // getters and setters

    @Override
    public String toString() {
        return "Track [id=" + id + ", title=" + title + ",artist=" + artist + ",code=" + code + "]";
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCode() {
        return code;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
