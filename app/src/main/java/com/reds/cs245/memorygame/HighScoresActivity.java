/*******************************************************************************
 *    file: HighScoresActivity.java
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
 * Purpose: creates the high scores page
 *
 /*******************************************************************************/

package com.reds.cs245.memorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HighScoresActivity extends AppCompatActivity {
    private int numCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        numCards = Integer.parseInt(sharedPref.getString(SettingsActivity.BOARD_SIZE_PREF_KEY, ""));
        //Boolean musicOn = sharedPref.getBoolean(SettingsActivity.MUSIC_PREF_KEY, true);

        TextView[] names = new TextView[5];
        TextView[] scores = new TextView[5];
        //final int numCards = ;   //Retrieve preference for number of cards.

        TextView numCardsView = (TextView) findViewById(R.id.numCards);
        numCardsView.setText("Game type: " + numCards + " cards.");

        names[0] = (TextView) findViewById(R.id.name0);
        names[1] = (TextView) findViewById(R.id.name1);
        names[2] = (TextView) findViewById(R.id.name2);
        names[3] = (TextView) findViewById(R.id.name3);
        names[4] = (TextView) findViewById(R.id.name4);

        scores[0] = (TextView) findViewById(R.id.score0);
        scores[1] = (TextView) findViewById(R.id.score1);
        scores[2] = (TextView) findViewById(R.id.score2);
        scores[3] = (TextView) findViewById(R.id.score3);
        scores[4] = (TextView) findViewById(R.id.score4);

        final ScoresRecord records = new ScoresRecord(numCards, getApplicationContext());
        for (int i = 0; i < 5; i++) {
            if (records.getScoreAt(i) == -1) {
                names[i].setText("");
                scores[i].setText("");
            }
            else {
                names[i].setText(records.getNameAt(i));
                scores[i].setText(String.valueOf(records.getScoreAt(i)));
            }
        }

        /*Button test = (Button) findViewById(R.id.testButton);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment df = GameOverDialog.newInstance(numCards, 50);
                df.show(getSupportFragmentManager(), "Tag");
            }
        });*/
    }
}
