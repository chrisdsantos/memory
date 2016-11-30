package com.reds.cs245.memorygame;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameOverDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameOverDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NUMCARDS = "numCards";
    private static final String ARG_SCORE = "score";

    // TODO: Rename and change types of parameters
    private int numCards;
    private int score;


    public GameOverDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param numCards Parameter 1.
     * @param score Parameter 2.
     * @return A new instance of fragment HighScoresEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameOverDialog newInstance(int numCards, int score) {
        GameOverDialog fragment = new GameOverDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMCARDS, numCards);
        args.putInt(ARG_SCORE, score);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numCards = getArguments().getInt(ARG_NUMCARDS);
            score = getArguments().getInt(ARG_SCORE);
        }
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_high_scores_entry, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tempTextView);
        tv.setText(String.valueOf(numCards));
        return v;
    }*/

    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.game_over_title)
                .setNegativeButton(R.string.game_over_negative,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Return to main menu.
                                startActivity(new Intent(getActivity(), TitleActivity.class));
                            }
                        }
                );
        if (score >= (new ScoresRecord(numCards, getContext())).getLowestScore()) {
            final View view = inflater.inflate(R.layout.dialog_high_score, null);
            ((TextView) view.findViewById(R.id.score))
                    .setText(String.format(getResources().getString(R.string.final_score), score));
            builder.setTitle("New High Score!")
                    .setView(view)
                    .setPositiveButton(R.string.alert_dialog_positive,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    EditText userIn = (EditText) view.findViewById(R.id.username);
                                    ScoresRecord records = new ScoresRecord(numCards, getActivity().getApplicationContext());
                                    records.addScore(userIn.getText().toString(), score);
                                    //Go to high scores.
                                    startActivity(new Intent(getActivity(), TitleActivity.class));
                                }
                            }
                    );
        }
        else {
            View view = inflater.inflate(R.layout.dialog_game_over, null);
            ((TextView) view.findViewById(R.id.score))
                    .setText(String.format(getResources().getString(R.string.final_score), score));
            builder.setView(view);
        }
        return builder.create();
    }

}
