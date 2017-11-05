package edu.urfu.mobile.geoquest;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class QuestActivity extends AppCompatActivity {

    private final String LOG_TAG = "myLogs";
    private final int REQUEST_CODE = 1;

    private SaveInstance save;

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button backButton;
    private Button answerButton;
    private Button clearInfoButton;
    private TextView questionTextView;
    private TextView countAnswers;
    private Question[] questionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int currentIndex = 0;
    private int countUsingAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        save = new SaveInstance(QuestActivity.this);
        Toast.makeText(QuestActivity.this, getIntent().getAction(), Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_quest);
        Log.d(LOG_TAG, "onCreate");

        trueButton = (Button) findViewById(R.id.true_button);
        falseButton = (Button) findViewById(R.id.false_button);
        nextButton = (Button) findViewById(R.id.next_button);
        backButton = (Button) findViewById(R.id.back_button);
        answerButton = (Button) findViewById(R.id.answer_button);
        clearInfoButton = (Button) findViewById(R.id.clear_button);

        questionTextView = (TextView) findViewById(R.id.question_text_view);
        countAnswers = (TextView) findViewById(R.id.countUsingQuestion_text_view);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questionBank.length;
                UpdateQuestion();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0) {
                    currentIndex = (currentIndex - 1) % questionBank.length;
                } else {
                    currentIndex = questionBank.length - 1;
                }
                UpdateQuestion();
            }
        });

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.SaveShowAnswer(false);
                if (countUsingAnswers < 3) {
                    //Intent intentAnswerActivity = new Intent(QuestActivity.this, AnswerActivity.class);
                    Intent intentAnswerActivity = new Intent("edu.urfu.mobile.geoquest.intent.action.CHEAT");
                    intentAnswerActivity.putExtra("answer", questionBank[currentIndex].IsAnswerTrue());
                    startActivityForResult(intentAnswerActivity, REQUEST_CODE);
                }
                else {
                    Toast.makeText(QuestActivity.this, R.string.not_answer, Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.Clear();
                currentIndex = 0;
                countUsingAnswers = 0;
                UpdateInstance();
            }
        });

        currentIndex = save.GetLastQuestion();
        countUsingAnswers = save.GetCountAnswers();
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt("currentIndexQuestion", 0);
            countUsingAnswers = savedInstanceState.getInt("currentCountUsingAnswers", 0);
        }
        UpdateInstance();
        if (save.GetLastActivity().equals("AnswerActivity")) {
            Intent intentAnswerActivity = new Intent("edu.urfu.mobile.geoquest.intent.action.CHEAT");
            intentAnswerActivity.putExtra("answer", questionBank[currentIndex].IsAnswerTrue());
            startActivityForResult(intentAnswerActivity, REQUEST_CODE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        save.SaveCountAnswers(countUsingAnswers);
        save.SaveLastActivity("QuestACtivity");
        save.SaveLastQuestion(currentIndex);
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        save.SaveCountAnswers(countUsingAnswers);
        save.SaveLastActivity("QuestACtivity");
        save.SaveLastQuestion(currentIndex);
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("currentIndexQuestion", currentIndex);
        outState.putInt("currentCountUsingAnswers", countUsingAnswers);
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    private void UpdateQuestion() {
        int question = questionBank[currentIndex].getTextResId();
        questionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerTrue = questionBank[currentIndex].IsAnswerTrue();
        int messageResId = 0;

        if (userPressedTrue == answerTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void UpdateCountUsingAnswers() {
        countAnswers.setText(Integer.toString(countUsingAnswers) + " " + getString(R.string.count_using_answers));
    }

    private void UpdateInstance() {
        UpdateCountUsingAnswers();
        UpdateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getBooleanExtra("clickAnswer", false)) {
                        countUsingAnswers++;
                        UpdateCountUsingAnswers();
                        save.SaveShowAnswer(false);
                    }
                    break;
                }
        }
    }
}

