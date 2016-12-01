/*******************************************************************************
 *    file: ScoresRecord.java
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
 * Purpose: class to record high scores
 *
 /*******************************************************************************/

package com.reds.cs245.memorygame;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Marc on 11/26/2016.
 */

public class ScoresRecord {
    private static final String JSON_NAMES = "names";
    private static final String JSON_SCORES = "scores";

    private ArrayList<String> names;
    private ArrayList<Integer> scores;
    private int numCards;
    private Context context;

    public ScoresRecord(int numCards, Context context) {
        names = new ArrayList<>(5);
        scores = new ArrayList<>(5);
        this.context = context;
        this.numCards = numCards;
        try {
            this.readScores(numCards);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addScore(String name, int score) {
        int index;
        for(index = 0; scores.get(index) > score; index++);
        names.remove(4);
        scores.remove(4);
        names.add(index, name);
        scores.add(index, score);
        try {
            writeScores();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getLowestScore() {
        return scores.get(4);
    }

    public String getNameAt(int index) {
        return names.get(index);
    }

    public int getScoreAt(int index) {
        return scores.get(index);
    }

    private void readScores(int numCards) throws IOException, JSONException {
        String fileName = "scores" + String.valueOf(numCards) + ".json";
        //context.deleteFile(fileName);
        InputStream in;
        try {
            in = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            writeInitialFile();
            in = context.openFileInput(fileName);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder jsonString = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            jsonString.append(line);
        }
        JSONObject jsonNew = (JSONObject) new JSONTokener(jsonString.toString()).nextValue();
        this.fromJSON(jsonNew);

        if (reader != null) {
            reader.close();
        }
    }

    private void writeInitialFile() {
        names.clear();
        scores.clear();
        for (int i = 0; i < 5; i++) {
            names.add(i, "");
            scores.add(i, -1);
        }
        try {
            writeScores();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeScores() throws IOException, JSONException {
        String fileName = "scores" + String.valueOf(numCards) + ".json";
        JSONObject json = this.toJSON();

        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(fileName, context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(json.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        for (int i = 0; i < 5; i++) {
            json.put(JSON_NAMES + String.valueOf(i), names.get(i));
            json.put(JSON_SCORES + String.valueOf(i), scores.get(i));
        }

        return json;
    }

    private void fromJSON(JSONObject jsonNew) throws JSONException {
        names.clear();
        scores.clear();
        for (int i = 0; i < 5; i++) {
            names.add(i, jsonNew.getString(JSON_NAMES + String.valueOf(i)));
            scores.add(i, jsonNew.getInt(JSON_SCORES + String.valueOf(i)));
        }
    }
}
