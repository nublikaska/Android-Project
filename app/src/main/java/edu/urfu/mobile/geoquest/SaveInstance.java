package edu.urfu.mobile.geoquest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Денис on 14.10.2017.
 */

public class SaveInstance{
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static final String PREFS = "PREFS";
    private static final String COUNT_ANSWERS = "COUNT_ANSWERS";
    private static final String LAST_ACTIVITY = "LAST_ACTIVITY";
    private static final String LAST_QUESTION = "LAST_QUESTION";
    private static final String SHOW_ANSWER = "SHOW_ANSWER";

    public SaveInstance(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }


    public void SaveCountAnswers(int countAnswers) {
        editor.putInt(COUNT_ANSWERS, countAnswers);
        editor.apply();
    }

    public void SaveLastActivity(String activity) {
        editor.putString(LAST_ACTIVITY, activity);
        editor.apply();
    }

    public void SaveLastQuestion(int question) {
        editor.putInt(LAST_QUESTION, question);
        editor.apply();
    }

    public void SaveShowAnswer(boolean bol) {
        editor.putBoolean(SHOW_ANSWER, bol);
        editor.apply();
    }

    public int GetCountAnswers() {
        return prefs.getInt(COUNT_ANSWERS, 0);
    }

    public String GetLastActivity() {
        return prefs.getString(LAST_ACTIVITY, "QuestActivity");
    }

    public int GetLastQuestion() {
        return prefs.getInt(LAST_QUESTION, 0);
    }

    public boolean GetShowAnswer() {
        return prefs.getBoolean(SHOW_ANSWER, false);
    }

    public void Clear() {
        editor.putInt(COUNT_ANSWERS, 0);
        editor.putString(LAST_ACTIVITY, "QuestActivity");
        editor.putInt(LAST_QUESTION, 0);
        editor.putBoolean(SHOW_ANSWER, false);
        editor.apply();
    }
}
