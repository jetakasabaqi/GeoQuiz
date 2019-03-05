package jeta.android.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    //todo : Challenge 1 : Make Toast appear at the top of the screen, not the bottom.
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Button mFinishButton;
    private LinearLayout mRepeatLayout;
    private Button mRepeatButton;
    private Button mCheatButton;
    private TextView mCheatLeftTextView;


    private static final String TAG = "QuizActivity";

    private static final String KEY_INDEX = "index";

    private static final String CHEATER_INDEX = "cheater_index";

    private static final String EXTRA_ANSWER_IS_TRUE = "jeta.android.geoquiz.answer_is_true";

    private static final String EXTRA_ANSWER_SHOWN = "jeta.android.geoquiz.answer_shown";

    private static final int REQUEST_CODE_CHEAT = 0;

    private boolean mIsCheate;


    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_austalia, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true),
    };

    private int mCurrentIndex = 0;

    private int mCurrentRes = 0;

    private int mCurrentCheats = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) calledz");
        setContentView(R.layout.activity_quiz);

        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.previous_button);
        mQuestionTextView = findViewById(R.id.question_text);
        mFinishButton = findViewById(R.id.finish);
        mRepeatLayout = findViewById(R.id.repeat);
        mRepeatButton = findViewById(R.id.play);
        mCheatButton = findViewById(R.id.cheat_button);
        mCheatLeftTextView = findViewById(R.id.cheats_left_text_view);
        if (mCurrentCheats == 0) {
            mCheatButton.setVisibility(View.INVISIBLE);
            mCheatLeftTextView.setText("No more cheats left!");
        } else {
            mCheatLeftTextView.setText(mCurrentCheats + " more cheats left");
        }
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheate = savedInstanceState.getBoolean(CHEATER_INDEX, false);
        }


        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        });
        if (mRepeatButton != null) {
            mRepeatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentIndex = 0;
                    mCurrentRes = 0;
                    mRepeatLayout.setVisibility(View.GONE);
                    updateQuestion();
                    mFinishButton.setVisibility(View.GONE);
                    mTrueButton.setEnabled(true);
                    mFalseButton.setEnabled(true);
                    mNextButton.setVisibility(View.VISIBLE);
                }
            });
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                mIsCheate = false;


                if (mCurrentIndex == 5) {
                    mNextButton.setVisibility(View.GONE);
                    mFinishButton.setVisibility(View.VISIBLE);

                    mFinishButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calculate();
                            mRepeatLayout.setVisibility(View.VISIBLE);

                        }
                    });

                }

                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);

            }
        });


        if (mCurrentIndex == 0) {
            if (mPrevButton != null)
                mPrevButton.setVisibility(View.GONE);

        } else {

            mPrevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;


                    returnQuestion();
                    mTrueButton.setEnabled(true);
                    mFalseButton.setEnabled(true);
                }
            });


        }
        if (mFinishButton != null)
            mFinishButton.setVisibility(View.GONE);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });


        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void returnQuestion() {

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheate) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mCurrentRes = mCurrentRes + 1;
            } else {
                messageResId = R.string.incorrect_toast;
            }

        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void calculate() {

        Toast.makeText(this, "You answered " + mCurrentRes + " questions right", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() calledz");
        if (mCurrentCheats == 0) {
            mCheatButton.setVisibility(View.INVISIBLE);
            mCheatLeftTextView.setText("No more cheats left!");
        } else {
            mCheatLeftTextView.setText(mCurrentCheats + " more cheats left");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() calledz");
        if (mCurrentCheats == 0) {
            mCheatButton.setVisibility(View.INVISIBLE);
            mCheatLeftTextView.setText("No more cheats left!");
        } else {
            mCheatLeftTextView.setText(mCurrentCheats + " more cheats left");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() calledz");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() calledz");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(CHEATER_INDEX, mIsCheate);
    }

    public static Intent newIntent(Context packageContex, boolean answerIsTrue) {
        Intent intent = new Intent(packageContex, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }

            mIsCheate = wasAnswerShown(data);

            if (mIsCheate) {
                mCurrentCheats = mCurrentCheats - 1;
            }
        }
    }
}
