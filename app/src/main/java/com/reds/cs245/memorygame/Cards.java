package com.reds.cs245.memorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Cards extends Fragment implements View.OnClickListener{

    private Cards cards;
    private Button[] cardsButton = new Button[20];
    private Button tryAgainButton;
    private Button newGameButton;
    private Button endGameButton;
    private TextView scoreView;
    private static String[] words = new String[20];
    private static int[][] cardMatches = new int[10][2];
    private boolean firstCardFlipped = false;
    private int firstCard;
    private int secondCard;
    private int[] cardID;
    private int score;
    private String[] visibleWords;
    private boolean[] isShown;

    public Cards() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            firstCardFlipped = savedInstanceState.getBoolean("firstCardFlipped");
            firstCard = savedInstanceState.getInt("firstCard");
            secondCard = savedInstanceState.getInt("secondCard");
            cardID = savedInstanceState.getIntArray("cardID");
            words = savedInstanceState.getStringArray("words");
            visibleWords = savedInstanceState.getStringArray("visibleWords");
            isShown = savedInstanceState.getBooleanArray("isShown");

        } else {
            try{
                randomize();
                cards = new Cards();
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cardID = new int[] {
                R.id.ButtonA1, R.id.ButtonA2, R.id.ButtonA3, R.id.ButtonA4,
                R.id.ButtonB1, R.id.ButtonB2, R.id.ButtonB3, R.id.ButtonB4,
                R.id.ButtonC1, R.id.ButtonC2, R.id.ButtonC3, R.id.ButtonC4,
                R.id.ButtonD1, R.id.ButtonD2, R.id.ButtonD3, R.id.ButtonD4,
                R.id.ButtonE1, R.id.ButtonE2, R.id.ButtonE3, R.id.ButtonE4
        };

        View myView = inflater.inflate(R.layout.fragment_cards, container, false);

        newGameButton = (Button) myView.findViewById(R.id.button_newgame);
        newGameButton.setOnClickListener(this);

        endGameButton = (Button) myView.findViewById(R.id.button_endgame);
        endGameButton.setOnClickListener(this);

        tryAgainButton = (Button) myView.findViewById(R.id.button_tryagain);
        tryAgainButton.setOnClickListener(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String boardSize = sharedPref.getString(SettingsActivity.BOARD_SIZE_PREF_KEY, "20");
        int size = Integer.parseInt(boardSize);

        for(int i = 0; i < 20; i++){
            cardsButton[i] = (Button) myView.findViewById(cardID[i]); //cards as buttons
            if(i >= size){
                cardsButton[i].setVisibility(View.INVISIBLE);
            }
            cardsButton[i].setOnClickListener(this);
        }

        scoreView = (TextView) myView.findViewById(R.id.score_display);
        scoreView.setText("Score: " + String.valueOf(score));

        return myView;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(tryAgainButton)){
            tryAgain();
        }else if(v.equals(endGameButton)){
            //System.out.println("END GAME");
            endGame();
        }else if(v.equals(newGameButton)){
            //System.out.println("NEW GAME");
            newGame();
        }else{
            ((Button) v).setText(words[getButtonIndex(v.getId())]);
            isShown[getButtonIndex(v.getId())] = true;
            if(firstCardFlipped == false){
                oneCardFlipped(v);
            }else{
                twoCardsFlipped(v);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("firstCardFlipped", firstCardFlipped);
        savedInstanceState.putInt("firstCard", firstCard);
        savedInstanceState.putInt("secondCard", secondCard);
        savedInstanceState.putIntArray("cardID", cardID);
        savedInstanceState.putStringArray("words", words);
        savedInstanceState.putBooleanArray("isShown", isShown);
        visibleWords = new String[20];
        savedInstanceState.putStringArray("visibleWords", visibleWords);
    }

    //*** GAME STATES ***

    //method: tryAgain
    //purpose: set state for when try again button is pressed
    private void tryAgain(){
        ((Button) getView().findViewById(cardID[firstCard])).setText("");
        ((Button) getView().findViewById(cardID[secondCard])).setText("");
        noCardsFlipped();
    }

    //method: noCardsFlipped
    //purpose: set state for when zero out of two cards are flipped
    private void noCardsFlipped(){
        firstCard = -1;
        secondCard = -1;
        firstCardFlipped = false;
        tryAgainButton.setClickable(false);
        for(Button card : cardsButton){
            card.setClickable(true);
        }
    }

    //method: oneCardFlipped
    //purpose: set state for when one out of two cards are flipped
    private void oneCardFlipped(View v){
        firstCardFlipped = true;
        tryAgainButton.setClickable(false);
        for(Button card : cardsButton){
            card.setClickable(true);
        }
        firstCard = getButtonIndex(v.getId());
    }

    //method: twoCardsFlipped
    //purpose: set state for when two out of two cards are flipped
    private void twoCardsFlipped(View v){
        tryAgainButton.setClickable(true);
        for(Button card : cardsButton){
            card.setClickable(false);
        }
        secondCard = getButtonIndex(v.getId());
        if(isMatch(firstCard, secondCard)){
            //System.out.println("Give me points!");
            score += 2;
            scoreView.setText("Score: " + String.valueOf(score));
            if(!isAllCardsFlipped()) {
                //System.out.println("Not done.");
                noCardsFlipped();
            }else{
                //System.out.println("Game over!");
                endGame();
            }
        }else{
            //System.out.println("Not a match!");
            if (score > 0) {
                score--;
                scoreView.setText("Score: " + String.valueOf(score));
            }
        }
    }

    //method: isAllCardsFlipped
    //purpose: check if all cards have been flipped
    private boolean isAllCardsFlipped(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String boardSize = sharedPref.getString(SettingsActivity.BOARD_SIZE_PREF_KEY, "20");
        int size = Integer.parseInt(boardSize);
        for(int i = 0;i<size;i++){
            if(cardsButton[i].getText().equals("")){
                return false;
            }
        }
        return true;
    }

    //method: isMatch
    //purpose: check if two cards flipped are a match
    public boolean isMatch(int firstCard, int secondCard){
        if(words[firstCard].equals(words[secondCard])){
            return true;
        }else{
            return false;
        }
    }

    private void endGame() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String boardSize = sharedPref.getString(SettingsActivity.BOARD_SIZE_PREF_KEY, "20");
        int size = Integer.parseInt(boardSize);

        tryAgainButton.setClickable(false);

        for (int i = 0; i < size; i++) {
            cardsButton[i].setText(words[i]);
        }
        DialogFragment df = GameOverDialog.newInstance(size, score);
        df.show(getFragmentManager(),"TAG");
    }

    private void newGame() {
        randomize();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String boardSize = sharedPref.getString(SettingsActivity.BOARD_SIZE_PREF_KEY, "20");
        int size = Integer.parseInt(boardSize);

        firstCardFlipped = false;
        tryAgainButton.setClickable(true);
        endGameButton.setClickable(true);
        newGameButton.setClickable(true);
        scoreView.setText("Score: " + String.valueOf(score));

        for (int i = 0; i < size; i++) {
            cardsButton[i].setText("");
            cardsButton[i].setClickable(true);
        }
    }

    //*** FUNCTIONS ***

    //method: randomize
    //purpose: randomly assigns word to card
    public void randomize(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String boardSize = sharedPref.getString(SettingsActivity.BOARD_SIZE_PREF_KEY, "20");
        int size = Integer.parseInt(boardSize);

        score = 0;
        isShown = new boolean[20];
        for(int i = 0; i < size; i++){
            words[i] = "";
            isShown[i] = false;
        }

        Stack<Integer> buttonIndicies = new Stack<Integer>();
        for(int i = 0;i<size;i++){
            buttonIndicies.push(i);
        }
        Collections.shuffle(buttonIndicies);

        List<Integer> wordsList = Arrays.asList(
                R.string.word_0,
                R.string.word_1,
                R.string.word_2,
                R.string.word_3,
                R.string.word_4,
                R.string.word_5,
                R.string.word_6,
                R.string.word_7,
                R.string.word_8,
                R.string.word_9
        );

        Stack<Integer> wordStack = new Stack<>();
        wordStack.addAll(wordsList);
        Collections.shuffle(wordStack);

        for(int i = 0;i<size/2;i++){
            String word = getResources().getString(wordStack.pop());
            words[buttonIndicies.pop()] = word;
            words[buttonIndicies.pop()] = word;
        }

    }

    //method: getButtonIndex
    //purpose: retrieves index of given button ID
    private int getButtonIndex(int id) {
        switch(id) {
            case R.id.ButtonA1:
                return 0;
            case R.id.ButtonA2:
                return 1;
            case R.id.ButtonA3:
                return 2;
            case R.id.ButtonA4:
                return 3;
            case R.id.ButtonB1:
                return 4;
            case R.id.ButtonB2:
                return 5;
            case R.id.ButtonB3:
                return 6;
            case R.id.ButtonB4:
                return 7;
            case R.id.ButtonC1:
                return 8;
            case R.id.ButtonC2:
                return 9;
            case R.id.ButtonC3:
                return 10;
            case R.id.ButtonC4:
                return 11;
            case R.id.ButtonD1:
                return 12;
            case R.id.ButtonD2:
                return 13;
            case R.id.ButtonD3:
                return 14;
            case R.id.ButtonD4:
                return 15;
            case R.id.ButtonE1:
                return 16;
            case R.id.ButtonE2:
                return 17;
            case R.id.ButtonE3:
                return 18;
            case R.id.ButtonE4:
                return 19;
        }return 0;
    }
}