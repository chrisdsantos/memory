/*******************************************************************************
 *    file: TitleActivity.java
 *  author: Omar Rodriguez
 *          Nahid Enayatzadeh
 *          Marc Deaso
 *          Christopher Santos
 *          Jazmin Guerrero
 *   class: CS245 - Programming Graphical User Interfaces
 *
 *  assignment: Android APP
 *  date last modified: 12/1/2016
 *
 * Purpose: creates the title page with widgets and associated layout
 *
 /*******************************************************************************/

package com.reds.cs245.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
    }
    /** Called when the user clicks the Send button */
    public void startSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void startHighScoresActivity(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    public void startGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}