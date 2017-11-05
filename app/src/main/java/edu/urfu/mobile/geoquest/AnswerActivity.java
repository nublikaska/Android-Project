package edu.urfu.mobile.geoquest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AnswerActivity extends AppCompatActivity {

    private TextView answerTextView;
    private Button buttonAnswer;
    private boolean clickAnswer = false;
    private Intent intentResult;
    SaveInstance save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        save = new SaveInstance(AnswerActivity.this);
        Toast.makeText(AnswerActivity.this, getIntent().getAction(), Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_answer);

        intentResult = new Intent();

        answerTextView = (TextView) findViewById(R.id.TextView);
        buttonAnswer = (Button) findViewById(R.id.answer_button);

        if (save.GetShowAnswer()) {
            if (getIntent().getBooleanExtra("answer", false)) {
                answerTextView.setText(R.string.true_button);
            } else  {
                answerTextView.setText(R.string.false_button);
            }
            clickAnswer = true;
            intentResult.putExtra("clickAnswer", clickAnswer);
            setResult(Activity.RESULT_OK, intentResult);
        }

        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getBooleanExtra("answer", false)) {
                    answerTextView.setText(R.string.true_button);
                } else  {
                    answerTextView.setText(R.string.false_button);
                }
                clickAnswer = true;
                intentResult.putExtra("clickAnswer", clickAnswer);
                setResult(Activity.RESULT_OK, intentResult);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        save.SaveLastActivity("AnswerActivity");
            save.SaveShowAnswer(clickAnswer);
    }

    protected void onPause() {
        super.onPause();
        save.SaveLastActivity("AnswerActivity");
        save.SaveShowAnswer(clickAnswer);
    }
}
