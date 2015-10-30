package com.juke.kynasaur.juke2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

    }

    public void player (View v) {
        startActivity(new Intent(Home.this, MainActivity.class));
    }

    public void search (View v) { startActivity(new Intent(Home.this, Searchable.class));}

}
