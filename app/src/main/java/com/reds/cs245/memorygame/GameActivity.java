/*******************************************************************************
 *    file: Cards.java
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
 * Purpose: creates the card (game) activity page with the card fragment
 *
 /*******************************************************************************/

package com.reds.cs245.memorygame;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


public class GameActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}